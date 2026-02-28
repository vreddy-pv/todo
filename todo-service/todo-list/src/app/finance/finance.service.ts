import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';

export interface Account {
  id?: number;
  name: string;
  balance: number;
}

export interface Transaction {
  id?: number;
  accountId: number;
  amount: number;
  date: string; // ISO date
  description?: string;
  type: string;
}

@Injectable({ providedIn: 'root' })
export class FinanceService {
  private readonly apiUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}/accounts`);
  }

  createAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(`${this.apiUrl}/accounts`, account);
  }

  updateAccount(id: number, updates: Partial<Account>): Observable<Account> {
    return this.http.patch<Account>(`${this.apiUrl}/accounts/${id}`, updates);
  }

  deleteAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/accounts/${id}`);
  }

  getTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/transactions`);
  }

  createTransaction(tx: Transaction): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/transactions`, tx);
  }

  updateTransaction(id: number, updates: Partial<Transaction>): Observable<Transaction> {
    return this.http.patch<Transaction>(`${this.apiUrl}/transactions/${id}`, updates);
  }

  deleteTransaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/transactions/${id}`);
  }
}
