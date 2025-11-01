import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CategoryService } from '../../../../core/services/category.service';
import { Category } from '../../../../shared/models/category';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-categories-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="row">
      <h2 style="margin:0">Usługi</h2>
      <span class="spacer"></span>
      <a routerLink="/categories/new"><button class="primary">+ Dodaj</button></a>
    </div>

    <ng-container *ngIf="categories$ | async as services; else loading">
      <div class="card">
        <p *ngIf="services.length === 0">Brak usług.</p>
        <ul *ngIf="services.length > 0">
          <li *ngFor="let s of services; trackBy: trackById">
            <a [routerLink]="['/categories', s.id]">{{ s.name }}</a>
            <span> — {{ s.durationMin }} min, {{ s.price | number:'1.2-2' }} PLN</span>
            <span> | </span>
            <a [routerLink]="['/categories', s.id, 'edit']">edytuj</a>
            <span> | </span>
            <button (click)="remove(s.id)">usuń</button>
          </li>
        </ul>
      </div>
    </ng-container>

    <ng-template #loading>
      <div class="card"><p>Ładowanie…</p></div>
    </ng-template>
  `
})
export class CategoriesListPage implements OnInit {
  categories$!: Observable<Category[]>;
  constructor(private api: CategoryService) {}
  ngOnInit(): void { this.categories$ = this.api.list(); }
  remove(id: string): void {
    this.api.delete(id).subscribe(() => this.categories$ = this.api.list());
  }
  trackById(_i: number, item: Category) { return item.id; }
}
