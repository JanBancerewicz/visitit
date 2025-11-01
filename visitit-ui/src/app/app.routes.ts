import { Routes } from '@angular/router';
import { CategoriesListPage } from './features/categories/pages/categories-list/categories-list.page';
import { CategoryNewPage } from './features/categories/pages/category-new/category-new.page';
import { CategoryEditPage } from './features/categories/pages/category-edit/category-edit.page';
import { CategoryDetailsPage } from './features/categories/pages/category-details/category-details.page';
import { ElementNewPage } from './features/elements/pages/element-new/element-new.page';
import { ElementEditPage } from './features/elements/pages/element-edit/element-edit.page';
import { ElementDetailsPage } from './features/elements/pages/element-details/element-details.page';

export const routes: Routes = [
  { path: '', redirectTo: 'categories', pathMatch: 'full' },
  { path: 'categories', component: CategoriesListPage },
  { path: 'categories/new', component: CategoryNewPage },
  { path: 'categories/:catId/edit', component: CategoryEditPage },
  { path: 'categories/:catId', component: CategoryDetailsPage },
  { path: 'categories/:catId/elements/new', component: ElementNewPage },
  { path: 'categories/:catId/elements/:elId/edit', component: ElementEditPage },
  { path: 'categories/:catId/elements/:elId', component: ElementDetailsPage },
  { path: '**', redirectTo: 'categories' }
];
