import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { RoomService } from '../../../../core/services/room.service';

@Component({
  selector: 'app-room-new',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="row">
      <a routerLink="/rooms">← Wróć</a>
      <span class="spacer"></span>
      <button class="primary" (click)="save()" [disabled]="form.invalid">Zapisz</button>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="save()">
        <label>Nazwa <input formControlName="name"/></label>
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
      </form>
    </div>
  `
})
export class RoomNewPage {
  private fb = inject(FormBuilder);
  form = this.fb.nonNullable.group({ name: ['', Validators.required] });

  constructor(private api: RoomService, private router: Router) {}
  save(): void {
    if (this.form.invalid) return;
    this.api.create(this.form.getRawValue()).subscribe(() => this.router.navigate(['/rooms']));
  }
}
