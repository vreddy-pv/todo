import { Injectable, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, map } from 'rxjs';

export interface Todo {
  id?: number;
  title: string;
  details?: string;
  completed?: boolean;
}

@Injectable({ providedIn: 'root' })
export class TodoService {
  private readonly apiUrl = 'http://localhost:8081/todos';

  constructor(private http: HttpClient) {}

  getTodos(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.apiUrl);
  }

  createTodo(todo: Todo): Observable<Todo> {
    return this.http.post<Todo>(this.apiUrl, todo);
  }

  updateTodo(id: number, updates: Partial<Todo>): Observable<Todo> {
    return this.http.patch<Todo>(`${this.apiUrl}/${id}`, updates);
  }

  deleteTodo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

@Component({
  selector: 'app-todo-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css']
})
export class TodoListComponent implements OnInit {
  todos: Todo[] = [];
  newTitle = '';
  newDetails = '';

  constructor(private todoService: TodoService) {}

  ngOnInit() {
    this.loadTodos();
  }

  loadTodos() {
    this.todoService.getTodos().subscribe({
      next: (data) => (this.todos = data),
      error: (e) => console.error('Failed to load todos', e)
    });
  }

  addTodo() {
    if (!this.newTitle.trim()) return;
    const todo: Todo = { title: this.newTitle, details: this.newDetails };
    this.todoService.createTodo(todo).subscribe({
      next: () => {
        this.newTitle = '';
        this.loadTodos();
		// clear both after add
		this.newTitle = '';
		this.newDetails = '';
      },
      error: (e) => console.error('Add failed', e)
    });
  }

  toggleComplete(todo: Todo) {
    if (!todo.id) return;
    this.todoService.updateTodo(todo.id, { completed: !todo.completed }).subscribe({
      next: () => this.loadTodos(),
      error: (e) => console.error('Update failed', e)
    });
  }

  delete(todo: Todo) {
    if (!todo.id) return;
    this.todoService.deleteTodo(todo.id).subscribe({
      next: () => this.loadTodos(),
      error: (e) => console.error('Delete failed', e)
    });
  }
}
