import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ElementService, Reservation } from '../../../../core/services/element.service';

@Component({
  selector: 'app-element-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <a [routerLink]="['/categories', serviceId]">← Wróć</a>
    <div class="card" *ngIf="reservation as r">
      <h2 style="margin-top:0">Rezerwacja #{{ r.id }}</h2>
      <p>Klient: {{ r.clientId }}</p>
      <p>Pracownik: {{ r.employeeId }}</p>
      <p>Sala: {{ r.roomId }}</p>
      <p>Od: {{ r.startDatetime }}</p>
      <p>Do: {{ r.endDatetime }}</p>
      <p>Status: {{ r.status }}</p>
      <p *ngIf="r.note">Notatka: {{ r.note }}</p>
    </div>
  `
})
export class ElementDetailsPage implements OnInit {
  serviceId = '';
  reservationId = '';
  reservation: Reservation | null = null;
  constructor(private route: ActivatedRoute, private api: ElementService) {}
  ngOnInit(): void {
    this.serviceId = String(this.route.snapshot.paramMap.get('catId') ?? '');
    this.reservationId = String(this.route.snapshot.paramMap.get('elId') ?? '');
    this.api.get(this.serviceId, this.reservationId).subscribe(r => this.reservation = r);
  }
}
