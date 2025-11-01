import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { RoomService } from '../../../../core/services/room.service';
import { Room } from '../../../../shared/models/room';

@Component({
  selector: 'app-rooms-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="row">
      <h2 style="margin:0">Pokoje</h2>
      <span class="spacer"></span>
      <a routerLink="/rooms/new"><button class="primary">+ Dodaj</button></a>
    </div>

    <div class="card">
      <p *ngIf="!rooms.length">Brak pokoi.</p>
      <ul *ngIf="rooms.length">
        <li *ngFor="let r of rooms; trackBy: trackById">
          {{ r.name }}
          <span> | </span>
          <a [routerLink]="['/rooms', r.id, 'edit']">edytuj</a>
          <span> | </span>
          <button (click)="remove(r.id)">usuń</button>
        </li>
      </ul>
    </div>
  `
})
export class RoomsListPage {
  rooms: Room[] = [];
  constructor(private api: RoomService) { this.reload(); }

  reload(): void { this.api.list().subscribe(list => this.rooms = list); }
  trackById(_i: number, r: Room) { return r.id; }
  remove(id: string): void {
    if (!confirm('Usunąć pokój?')) return;
    this.api.delete(id).subscribe(() => this.reload());
  }
}
