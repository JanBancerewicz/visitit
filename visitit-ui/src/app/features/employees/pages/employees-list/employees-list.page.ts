import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EmployeeService } from '../../../../core/services/employee.service';
import { Employee } from '../../../../shared/models/employee';

@Component({
  selector: 'app-employees-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="row">
      <h2 style="margin:0">Pracownicy</h2>
      <span class="spacer"></span>
      <a routerLink="/employees/new"><button class="primary">+ Dodaj</button></a>
    </div>

    <div class="card">
      <p *ngIf="!employees.length">Brak pracowników.</p>
      <ul *ngIf="employees.length">
        <li *ngFor="let e of employees; trackBy: trackById">
          {{ e.firstName }} {{ e.lastName }} — {{ e.role }}
          <span> | </span>
          <a [routerLink]="['/employees', e.id, 'edit']">edytuj</a>
          <span> | </span>
          <button (click)="remove(e.id)">usuń</button>
        </li>
      </ul>
    </div>
  `
})
export class EmployeesListPage {
  employees: Employee[] = [];
  constructor(private api: EmployeeService) { this.reload(); }

  reload(): void { this.api.list().subscribe(list => this.employees = list); }
  trackById(_i: number, e: Employee) { return e.id; }
  remove(id: string): void {
    if (!confirm('Usunąć pracownika?')) return;
    this.api.delete(id).subscribe(() => this.reload());
  }
}
