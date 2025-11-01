import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../../../../core/services/category.service';
import { Category } from '../../../../shared/models/category';

@Component({
  selector: 'app-category-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2>Edycja usługi</h2>
    <form [formGroup]="form" (ngSubmit)="save()">
      <label>Nazwa</label>
      <input formControlName="name" />

      <label>Opis</label>
      <input formControlName="description" />

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
export class CategoryEditPage implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(CategoryService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.maxLength(128)]],
    description: [''],
    durationMin: [30, [Validators.required, Validators.min(1)]],
    price: [0, [Validators.required, Validators.min(0)]],
  });

  private id = '';

  ngOnInit(): void {
    this.id = String(this.route.snapshot.paramMap.get('catId') ?? '');
    this.api.get(this.id).subscribe((s: Category) => this.form.patchValue(s));
  }
  save(): void {
    const dto = this.form.getRawValue();
    this.api.update(this.id, dto).subscribe({
      next: () => this.router.navigate(['/categories']),
      error: () => alert('Błąd aktualizacji usługi.')
    });
  }
  cancel(): void { this.router.navigate(['/categories']); }
}
