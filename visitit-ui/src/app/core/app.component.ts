import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  template: `
    <header class="topnav">
      <a routerLink="/" class="brand">VisitIt</a>
      <nav>
        <a routerLink="/categories" routerLinkActive="active">Usługi</a>
        <a routerLink="/clients" routerLinkActive="active">Klienci</a>
        <a routerLink="/employees" routerLinkActive="active">Pracownicy</a>
        <a routerLink="/rooms" routerLinkActive="active">Pokoje</a>
      </nav>
    </header>

    <main class="container">
      <router-outlet />
    </main>
  `,
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {}
