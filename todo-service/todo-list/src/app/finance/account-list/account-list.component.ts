import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FinanceService, Account } from '../finance.service';

@Component({
  selector: 'app-account-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './account-list.component.html',
  styleUrls: ['./account-list.component.css']
})
export class AccountListComponent implements OnInit {
  accounts: Account[] = [];
  newName = '';
  newBalance = 0;

  constructor(private financeService: FinanceService) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.financeService.getAccounts().subscribe({
      next: (data) => (this.accounts = data),
      error: (e) => console.error('Failed to load accounts', e)
    });
  }

  addAccount(): void {
    if (!this.newName.trim()) return;
    const acc: Account = { name: this.newName, balance: this.newBalance };
    this.financeService.createAccount(acc).subscribe({
      next: () => {
        this.newName = '';
        this.newBalance = 0;
        this.loadAccounts();
      },
      error: (e) => console.error('Add failed', e)
    });
  }

  delete(acc: Account): void {
    if (!acc.id) return;
    this.financeService.deleteAccount(acc.id).subscribe({
      next: () => this.loadAccounts(),
      error: (e) => console.error('Delete failed', e)
    });
  }
}
