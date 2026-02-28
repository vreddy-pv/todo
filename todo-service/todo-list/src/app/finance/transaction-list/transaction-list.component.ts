import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FinanceService, Transaction } from '../finance.service';

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {
  transactions: Transaction[] = [];
  newAccountId = 0;
  newAmount = 0;
  newDate = '';
  newType: string = 'CREDIT';
  newDescription: string = '';

  constructor(private financeService: FinanceService) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.financeService.getTransactions().subscribe({
      next: (data) => (this.transactions = data),
      error: (e) => console.error('Failed to load transactions', e)
    });
  }

  addTransaction(): void {
    if (!this.newAccountId || !this.newDate) return;
    const tx: Transaction = {
      type: this.newType,
      accountId: this.newAccountId,
      amount: this.newAmount,
      date: this.newDate,
      description: this.newDescription
    };
    this.financeService.createTransaction(tx).subscribe({
      next: () => {
        this.newAccountId = 0;
        this.newAmount = 0;
        this.newDate = '';
        this.newDescription = '';
        this.loadTransactions();
      },
      error: (e) => console.error('Add failed', e)
    });
  }

  delete(tx: Transaction): void {
    if (!tx.id) return;
    this.financeService.deleteTransaction(tx.id).subscribe({
      next: () => this.loadTransactions(),
      error: (e) => console.error('Delete failed', e)
    });
  }
}
