import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-login-page',
  imports: [FormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent {
  readonly isRegistering = signal(false);
  readonly isSubmitting = signal(false);
  readonly error = signal('');
  name = '';
  email = '';
  password = '';

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router
  ) {}

  submit(): void {
    this.error.set('');
    this.isSubmitting.set(true);

    if (this.isRegistering()) {
      this.auth.register(this.name, this.email, this.password).subscribe({
        next: () => {
          this.isRegistering.set(false);
          this.isSubmitting.set(false);
        },
        error: (response) => this.handleError(response)
      });
      return;
    }

    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        void this.router.navigateByUrl('/boards');
      },
      error: (response) => this.handleError(response)
    });
  }

  toggleMode(): void {
    this.error.set('');
    this.isRegistering.update((value) => !value);
  }

  private handleError(response: { error?: { message?: string } }): void {
    this.isSubmitting.set(false);
    this.error.set(response.error?.message ?? 'Não foi possível concluir a solicitação.');
  }
}
