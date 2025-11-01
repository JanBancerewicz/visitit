import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CategoryService } from '../../../../core/services/category.service';

@Component({
  selector: 'app-category-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2>Nowa usługa</h2>
    <form [formGroup]="form" (ngSubmit)="save()">
      <label>Nazwa</label>
      <input formControlName="name" placeholder="np. Strzyżenie męskie" />

      <label>Opis</label>
      <input formControlName="description" placeholder="opcjonalnie" />

      <label>Czas trwania (min)</label>
      <input type="number" formControlName="durationMin" min="1" />

      <label>Cena (PLN)</label>
      <input type="number" step="0.01" formControlName="price" min="0" />

      <div class="row">
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
        <button type="button" (click)="cancel()">Anuluj</button>
      </div>
    </form>
  `
})
export class CategoryNewPage {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(CategoryService);
  private readonly router = inject(Router);

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(128)]],
    description: [''],
    durationMin: [30, [Validators.required, Validators.min(1)]],
    price: [0, [Validators.required, Validators.min(0)]],
  });

  save(): void {
    const dto = this.form.getRawValue();
    this.api.create(dto).subscribe({
      next: () => this.router.navigate(['/categories']),
      error: () => alert('Błąd tworzenia usługi. Sprawdź konsolę i backend.')
    });
  }
  cancel(): void { this.router.navigate(['/categories']); }
}
