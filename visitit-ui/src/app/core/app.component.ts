import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <header class="card container">
      <div class="row">
        <h1 style="margin:0">VisitIt</h1>
        <span class="spacer"></span>
        <a routerLink="/categories">Kategorie</a>
      </div>
    </header>
    <main class="container">
      <router-outlet />
    </main>
  `
})
export class AppComponent {}
