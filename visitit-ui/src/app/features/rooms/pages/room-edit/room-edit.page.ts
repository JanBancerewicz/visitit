import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { RoomService } from '../../../../core/services/room.service';

@Component({
  selector: 'app-room-edit',
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
export class RoomEditPage implements OnInit {
  private fb = inject(FormBuilder);
  id = '';
  form = this.fb.nonNullable.group({ name: ['', Validators.required] });

  constructor(private route: ActivatedRoute, private api: RoomService, private router: Router) {}
  ngOnInit(): void {
    this.id = String(this.route.snapshot.paramMap.get('id') ?? '');
    this.api.get(this.id).subscribe(r => this.form.patchValue(r));
  }
  save(): void {
    if (this.form.invalid) return;
    this.api.update(this.id, this.form.getRawValue()).subscribe(() => this.router.navigate(['/rooms']));
  }
}
