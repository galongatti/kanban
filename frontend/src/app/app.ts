import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LoadingService } from './core/loading.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  template: `
    <router-outlet />
    @if (loading.isLoading()) {
      <div class="loading-overlay" role="status" aria-live="polite">
        <span class="loading-spinner" aria-hidden="true"></span>
        <span>Carregando...</span>
      </div>
    }
  `,
  styles: `
    .loading-overlay {
      position: fixed;
      inset: 0;
      z-index: 100;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12px;
      background: rgb(21 38 31 / 42%);
      color: #fff;
      font-weight: 700;
    }

    .loading-spinner {
      width: 24px;
      height: 24px;
      border: 3px solid rgb(255 255 255 / 35%);
      border-top-color: #b9e05c;
      border-radius: 50%;
      animation: loading-spin .7s linear infinite;
    }

    @keyframes loading-spin {
      to { transform: rotate(360deg); }
    }
  `
})
export class App {
  constructor(readonly loading: LoadingService) {}
}
