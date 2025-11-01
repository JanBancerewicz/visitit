import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { EmployeeService } from '../../../../core/services/employee.service';

@Component({
  selector: 'app-employee-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="row">
      <a routerLink="/employees">← Wróć</a>
      <span class="spacer"></span>
      <button class="primary" (click)="save()" [disabled]="form.invalid">Zapisz</button>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="save()">
        <label>Imię <input formControlName="firstName"/></label>
        <label>Nazwisko <input formControlName="lastName"/></label>
        <label>Rola <input formControlName="role"/></label>
        <label>Opis <input formControlName="description"/></label>
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
      </form>
    </div>
  `
})
export class EmployeeNewPage {
  private fb = inject(FormBuilder);
  form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    role: ['', Validators.required],
    description: ['']
  });

  constructor(private api: EmployeeService, private router: Router) {}
  save(): void {
    if (this.form.invalid) return;
    this.api.create(this.form.getRawValue()).subscribe(() => this.router.navigate(['/employees']));
  }
}
