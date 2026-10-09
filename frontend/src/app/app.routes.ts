import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { BoardPageComponent } from './features/board-page.component';
import { LoginPageComponent } from './features/login-page.component';

export const routes: Routes = [
  { path: 'login', component: LoginPageComponent },
  { path: 'boards', component: BoardPageComponent, canActivate: [authGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'boards' },
  { path: '**', redirectTo: 'boards' }
];
