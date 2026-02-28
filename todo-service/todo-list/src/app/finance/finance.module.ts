import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { FinanceDashboardComponent } from './finance-dashboard/finance-dashboard.component';
import { AccountListComponent } from './account-list/account-list.component';
import { TransactionListComponent } from './transaction-list/transaction-list.component';

const routes: Routes = [
  { path: '', component: FinanceDashboardComponent, children: [
    { path: 'accounts', component: AccountListComponent },
    { path: 'transactions', component: TransactionListComponent },
  ] },
];

@NgModule({
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class FinanceModule {}