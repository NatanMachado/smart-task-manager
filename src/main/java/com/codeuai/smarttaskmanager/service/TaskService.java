package com.codeuai.smarttaskmanager.service;

import com.codeuai.smarttaskmanager.dto.TaskRequest;
import com.codeuai.smarttaskmanager.dto.TaskResponse;
import com.codeuai.smarttaskmanager.exception.NotFoundException;
import com.codeuai.smarttaskmanager.model.Task;
import com.codeuai.smarttaskmanager.model.User;
import com.codeuai.smarttaskmanager.repository.TaskRepository;
import com.codeuai.smarttaskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService extends SessionService {

        private final TaskRepository taskRepository;
        private final UserRepository userRepository;

        @CacheEvict(value = "dashboard:getSummary", keyGenerator = "userKeyGenerator")
        public TaskResponse create(TaskRequest request) {
                String username = super.getCurrentUsername();
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new NotFoundException("User not found"));

                Task task = Task.builder()
                                .title(request.title())
                                .description(request.description())
                                .completed(request.completed())
                                .user(user)
                                .build();

                Task saved = taskRepository.save(task);

                return new TaskResponse(
                                saved.getId(),
                                saved.getTitle(),
                                saved.getDescription(),
                                saved.isCompleted());
        }

        public List<TaskResponse> findAll() {
                String username = super.getCurrentUsername();

                return taskRepository.findByUserUsername(username)
                                .stream()
                                .map(t -> new TaskResponse(
                                                t.getId(),
                                                t.getTitle(),
                                                t.getDescription(),
                                                t.isCompleted()))
                                .toList();
        }

        public Page<TaskResponse> findAllPaged(int page, int size, String sortBy) {
                String username = super.getCurrentUsername();

                PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));

                return taskRepository.findByUserUsername(username, pageable)
                                .map(t -> new TaskResponse(
                                                t.getId(),
                                                t.getTitle(),
                                                t.getDescription(),
                                                t.isCompleted()));
        }

        public TaskResponse findById(Long id) {
                String username = super.getCurrentUsername();

                Task task = taskRepository.findById(id)
                                .filter(t -> t.getUser().getUsername().equals(username))
                                .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

                return new TaskResponse(
                                task.getId(),
                                task.getTitle(),
                                task.getDescription(),
                                task.isCompleted());
        }

        public Page<TaskResponse> search(
                        int page,
                        int size,
                        String sortBy,
                        Boolean completed,
                        String query) {
                String username = super.getCurrentUsername();

                PageRequest pageable = PageRequest.of(page, size, Sort.by(sortBy));

                return taskRepository.search(username, completed, query, pageable)
                                .map(t -> new TaskResponse(
                                                t.getId(),
                                                t.getTitle(),
                                                t.getDescription(),
                                                t.isCompleted()));
        }

        @CacheEvict(value = "dashboard:getSummary", keyGenerator = "userKeyGenerator")
        public TaskResponse update(Long id, TaskRequest request) {
                String username = super.getCurrentUsername();

                Task task = taskRepository.findById(id)
                                .filter(t -> t.getUser().getUsername().equals(username))
                                .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

                task.setTitle(request.title());
                task.setDescription(request.description());
                task.setCompleted(request.completed());

                Task updated = taskRepository.save(task);

                return new TaskResponse(
                                updated.getId(),
                                updated.getTitle(),
                                updated.getDescription(),
                                updated.isCompleted());
        }

        @CacheEvict(value = "dashboard:getSummary", keyGenerator = "userKeyGenerator")
        public void delete(Long id) {
                String username = super.getCurrentUsername();

                Task task = taskRepository.findById(id)
                                .filter(t -> t.getUser().getUsername().equals(username))
                                .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

                taskRepository.delete(task);
        }
}