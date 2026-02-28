import { Routes } from '@angular/router';

import { TodoListComponent } from './todo-list/todo-list.component';

export const routes: Routes = [
  { path: '', component: TodoListComponent },
  { path: 'finance', loadChildren: () => import('./finance/finance.module').then(m => m.FinanceModule) },
  { path: '**', redirectTo: '' }
];
