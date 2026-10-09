import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from './models';

const TOKEN_KEY = 'kanban.token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  readonly isAuthenticated = signal(Boolean(localStorage.getItem(TOKEN_KEY)));

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>('/api/auth/login', { email, password }).pipe(
      tap(({ token }) => {
        localStorage.setItem(TOKEN_KEY, token);
        this.isAuthenticated.set(true);
      })
    );
  }

  register(name: string, email: string, password: string): Observable<unknown> {
    return this.http.post('/api/users', { name, email, password });
  }

  token(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    this.isAuthenticated.set(false);
    void this.router.navigateByUrl('/login');
  }
}
