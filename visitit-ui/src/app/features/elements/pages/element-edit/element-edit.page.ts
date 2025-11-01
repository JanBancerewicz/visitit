import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ElementService, Reservation, ReservationCreate } from '../../../../core/services/element.service';
import { ClientService } from '../../../../core/services/client.service';
import { EmployeeService } from '../../../../core/services/employee.service';
import { RoomService } from '../../../../core/services/room.service';
import { Client } from '../../../../shared/models/client';
import { Employee } from '../../../../shared/models/employee';
import { Room } from '../../../../shared/models/room';

@Component({
  selector: 'app-element-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="row">
      <a [routerLink]="['/categories', serviceId]">← Wróć</a>
      <span class="spacer"></span>
      <h3 style="margin:0">Edycja rezerwacji</h3>
    </div>

    <form class="card" [formGroup]="form" (ngSubmit)="save()">
      <label>Klient
        <select formControlName="clientId" required>
          <option value="" disabled>— wybierz —</option>
          <option *ngFor="let c of clients" [value]="c.id">
            {{ c.firstName }} {{ c.lastName }} ({{ c.email }})
          </option>
        </select>
      </label>

      <label>Pracownik
        <select formControlName="employeeId" required>
          <option value="" disabled>— wybierz —</option>
          <option *ngFor="let e of employees" [value]="e.id">
            {{ e.firstName }} {{ e.lastName }} — {{ e.role || '—' }}
          </option>
        </select>
      </label>

      <label>Pokój
        <select formControlName="roomId" required>
          <option value="" disabled>— wybierz —</option>
          <option *ngFor="let r of rooms" [value]="r.id">{{ r.name }}</option>
        </select>
      </label>

      <label>Start
        <input type="datetime-local" formControlName="startDatetime" required>
      </label>

      <label>Koniec
        <input type="datetime-local" formControlName="endDatetime" required>
      </label>

      <label>Status
        <select formControlName="status" required>
          <option value="PLANNED">PLANNED</option>
          <option value="CONFIRMED">CONFIRMED</option>
          <option value="DONE">DONE</option>
          <option value="CANCELLED">CANCELLED</option>
        </select>
      </label>

      <label>Notatka
        <textarea formControlName="note" rows="3"></textarea>
      </label>

      <div class="row">
        <button type="submit" class="primary" [disabled]="form.invalid">Zapisz</button>
      </div>
    </form>
  `
})
export class ElementEditPage implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private api = inject(ElementService);
  private clientsApi = inject(ClientService);
  private employeesApi = inject(EmployeeService);
  private roomsApi = inject(RoomService);

  serviceId = '';
  reservationId = '';
  clients: Client[] = [];
  employees: Employee[] = [];
  rooms: Room[] = [];

  form = this.fb.nonNullable.group({
    clientId: ['', Validators.required],
    employeeId: ['', Validators.required],
    roomId: ['', Validators.required],
    startDatetime: ['', Validators.required],
    endDatetime:   ['', Validators.required],
    status: ['', Validators.required],
    note: ['']
  });

  ngOnInit(): void {
    this.serviceId = String(this.route.snapshot.paramMap.get('catId') ?? '');
    this.reservationId = String(this.route.snapshot.paramMap.get('elId') ?? '');

    // opcje
    this.clientsApi.list().subscribe(x => this.clients = x);
    this.employeesApi.list().subscribe(x => this.employees = x);
    this.roomsApi.list().subscribe(x => this.rooms = x);

    // dane rezerwacji
    this.api.get(this.reservationId).subscribe((r: Reservation) => {
      this.form.patchValue({
        clientId: r.clientId,
        employeeId: r.employeeId,
        roomId: r.roomId,
        startDatetime: r.startDatetime?.slice(0, 16), // 'yyyy-MM-ddTHH:mm'
        endDatetime:   r.endDatetime?.slice(0, 16),
        status: r.status,
        note: r.note ?? ''
      });
    });
  }

  private toIsoSeconds(x: string): string {
    return x && x.length === 16 ? `${x}:00` : x;
  }

  save(): void {
    const v = this.form.getRawValue();
    const dto: ReservationCreate = {
      clientId: v.clientId,
      employeeId: v.employeeId,
      roomId: v.roomId,
      serviceId: this.serviceId,
      startDatetime: this.toIsoSeconds(v.startDatetime),
      endDatetime: this.toIsoSeconds(v.endDatetime),
      status: v.status,
      note: v.note || ''
    };
    this.api.update(this.reservationId, dto).subscribe(() =>
      this.router.navigate(['/categories', this.serviceId])
    );
  }
}
