import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ElementService } from '../../../../core/services/element.service';

@Component({
  selector: 'app-element-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2>Nowa rezerwacja</h2>
    <form [formGroup]="form" (ngSubmit)="save()">
      <label>Client ID</label>
      <input formControlName="clientId" />

      <label>Employee ID</label>
      <input formControlName="employeeId" />

      <label>Room ID</label>
      <input formControlName="roomId" />

      <label>Start</label>
      <input type="datetime-local" formControlName="startDatetime" />

      <label>End</label>
      <input type="datetime-local" formControlName="endDatetime" />

      <label>Status</label>
      <input formControlName="status" placeholder="np. PLANNED" />

      <label>Notatka</label>
      <input formControlName="note" />

      <div class="row">
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
        <button type="button" (click)="cancel()">Anuluj</button>
      </div>
    </form>
  `
})
export class ElementNewPage implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ElementService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  form = this.fb.nonNullable.group({
    clientId: ['', Validators.required],
    employeeId: ['', Validators.required],
    roomId: ['', Validators.required],
    startDatetime: ['', Validators.required],
    endDatetime: ['', Validators.required],
    status: ['PLANNED', Validators.required],
    note: [''],
  });

  private serviceId = '';

  ngOnInit(): void {
    this.serviceId = String(this.route.snapshot.paramMap.get('catId') ?? '');
  }

  save(): void {
    const dto = { ...this.form.getRawValue(), serviceId: this.serviceId };
    this.api.createReservation(dto).subscribe({
      next: () => this.router.navigate(['/categories', this.serviceId]),
      error: () => alert('Błąd tworzenia rezerwacji.')
    });
  }
  cancel(): void { this.router.navigate(['/categories', this.serviceId]); }
}
