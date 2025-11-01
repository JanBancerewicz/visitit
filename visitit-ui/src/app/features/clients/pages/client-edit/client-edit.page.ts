import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ClientService } from '../../../../core/services/client.service';

@Component({
  selector: 'app-client-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="row">
      <a routerLink="/clients">← Wróć</a>
      <span class="spacer"></span>
      <button class="primary" (click)="save()" [disabled]="form.invalid">Zapisz</button>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="save()">
        <label>Imię <input formControlName="firstName"/></label>
        <label>Nazwisko <input formControlName="lastName"/></label>
        <label>Email <input formControlName="email" type="email"/></label>
        <label>Telefon <input formControlName="phone"/></label>
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
      </form>
    </div>
  `
})
export class ClientEditPage implements OnInit {
  private fb = inject(FormBuilder);
  id = '';
  form = this.fb.nonNullable.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: ['']
  });

  constructor(private route: ActivatedRoute, private api: ClientService, private router: Router) {}
  ngOnInit(): void {
    this.id = String(this.route.snapshot.paramMap.get('id') ?? '');
    this.api.get(this.id).subscribe(c => this.form.patchValue(c));
  }
  save(): void {
    if (this.form.invalid) return;
    this.api.update(this.id, this.form.getRawValue()).subscribe(() => this.router.navigate(['/clients']));
  }
}
