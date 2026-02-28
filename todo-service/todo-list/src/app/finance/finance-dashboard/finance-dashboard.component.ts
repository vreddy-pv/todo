import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-finance-dashboard',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './finance-dashboard.component.html',
  styleUrls: ['./finance-dashboard.component.css']
})
export class FinanceDashboardComponent {
  title = 'Finance Dashboard';
}
