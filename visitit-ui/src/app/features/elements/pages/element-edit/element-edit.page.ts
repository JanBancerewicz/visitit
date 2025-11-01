import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ElementService, Reservation } from '../../../../core/services/element.service';

@Component({
  selector: 'app-element-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2>Edycja rezerwacji</h2>
    <form [formGroup]="form" (ngSubmit)="save()">
      <label>Status</label>
      <input formControlName="status" />

      <label>Notatka</label>
      <input formControlName="note" />

      <div class="row">
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
        <button type="button" (click)="cancel()">Anuluj</button>
      </div>
    </form>
  `
})
export class ElementEditPage implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(ElementService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  form = this.fb.nonNullable.group({
    status: ['PLANNED', Validators.required],
    note: [''],
  });

  private serviceId = '';
  private reservationId = '';

  ngOnInit(): void {
    this.serviceId = String(this.route.snapshot.paramMap.get('catId') ?? '');
    this.reservationId = String(this.route.snapshot.paramMap.get('elId') ?? '');
    this.api.get(this.serviceId, this.reservationId).subscribe((r: Reservation) => {
      this.form.patchValue({ status: r.status, note: r.note });
    });
  }

  save(): void {
    const dto = this.form.getRawValue();
    this.api.updateReservation(this.reservationId, dto).subscribe({
      next: () => this.router.navigate(['/categories', this.serviceId]),
      error: () => alert('Błąd aktualizacji rezerwacji.')
    });
  }
  cancel(): void { this.router.navigate(['/categories', this.serviceId]); }
}
