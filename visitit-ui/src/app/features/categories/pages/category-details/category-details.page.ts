import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { CategoryService } from '../../../../core/services/category.service';
import { ElementService, ReservationList } from '../../../../core/services/element.service';
import { Category } from '../../../../shared/models/category';

@Component({
  selector: 'app-category-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="row">
      <a routerLink="/categories">← Wróć</a>
      <span class="spacer"></span>
      <a [routerLink]="['/categories', id, 'elements', 'new']">
        <button>+ Dodaj rezerwację</button>
      </a>
    </div>

    <div class="card" *ngIf="service">
      <h2 style="margin-top:0">{{ service.name }}</h2>
      <p *ngIf="service.description">{{ service.description }}</p>
      <p>Czas: {{ service.durationMin }} min | Cena: {{ service.price | number:'1.2-2' }} PLN</p>
    </div>

    <div class="card">
      <h3 style="margin-top:0">Rezerwacje tej usługi</h3>

      <p *ngIf="loading">Ładowanie…</p>
      <p *ngIf="!loading && reservations.length === 0">Brak rezerwacji.</p>

      <ul *ngIf="!loading && reservations.length > 0">
        <li *ngFor="let r of reservations; trackBy: trackByResId">
          <a [routerLink]="['/categories', id, 'elements', r.id]">
            #{{ r.id }} — {{ r.clientName }} — {{ r.employeeName }} — {{ r.status }}
          </a>
          <span> | </span>
          <a [routerLink]="['/categories', id, 'elements', r.id, 'edit']">edytuj</a>
          <span> | </span>
          <button (click)="removeReservation(r.id)">usuń</button>
        </li>
      </ul>

      <p *ngIf="error" class="error">{{ error }}</p>
    </div>
  `
})
export class CategoryDetailsPage implements OnInit {
  id = '';
  service: Category | null = null;
  reservations: ReservationList[] = [];
  loading = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private cats: CategoryService,
    private resv: ElementService
  ) {}

  ngOnInit(): void {
    this.id = String(this.route.snapshot.paramMap.get('catId') ?? '');
    this.cats.get(this.id).subscribe(s => this.service = s);
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading = true;
    this.error = '';
    this.resv.listByCategory(this.id).subscribe({
      next: list => { this.reservations = list; this.loading = false; },
      error: () => { this.error = 'Nie udało się załadować rezerwacji.'; this.loading = false; }
    });
  }

  removeReservation(reservationId: string): void {
    if (!confirm('Na pewno usunąć tę rezerwację?')) return;
    this.resv.delete(reservationId).subscribe({
      next: () => this.loadReservations(),
      error: () => alert('Usuwanie nie powiodło się.')
    });
  }

  trackByResId(_i: number, r: ReservationList) { return r.id; }
}
