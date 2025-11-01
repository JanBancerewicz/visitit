import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ClientService } from '../../../../core/services/client.service';
import { Client } from '../../../../shared/models/client';

@Component({
  selector: 'app-clients-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="row">
      <h2 style="margin:0">Klienci</h2>
      <span class="spacer"></span>
      <a routerLink="/clients/new"><button class="primary">+ Dodaj</button></a>
    </div>

    <div class="card">
      <p *ngIf="!clients.length">Brak klientów.</p>
      <ul *ngIf="clients.length">
        <li *ngFor="let c of clients; trackBy: trackById">
          {{ c.firstName }} {{ c.lastName }} — {{ c.email }}
          <span> | </span>
          <a [routerLink]="['/clients', c.id, 'edit']">edytuj</a>
          <span> | </span>
          <button (click)="remove(c.id)">usuń</button>
        </li>
      </ul>
    </div>
  `
})
export class ClientsListPage {
  clients: Client[] = [];
  constructor(private api: ClientService) { this.reload(); }

  reload(): void { this.api.list().subscribe(list => this.clients = list); }
  trackById(_i: number, c: Client) { return c.id; }
  remove(id: string): void {
    if (!confirm('Usunąć klienta?')) return;
    this.api.delete(id).subscribe(() => this.reload());
  }
}
