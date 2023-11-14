import { Injectable, NgModule } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { RouterModule, RouterStateSnapshot, Routes, TitleStrategy } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { authGuard, authGuardLogin } from './core/guards/auth.guard';

const routes: Routes = [
  { 
    path: '', 
    title:  'Startseite',
    loadChildren: () => import('./modules/landing-page/landing-page.module').then((m) => m.LandingPageModule)
  },
  { 
    path: '',
    title:  'Einloggen',
    loadChildren: () => import('./modules/auth/auth.module').then((m) => m.AuthModule),
  },
  { 
    path: '',
    title:  'Einloggen',
    loadChildren: () => import('./statments/statments.module').then((m) => m.StatmentsModule),
  },
  { 
    path: '',
    title:  'Registrieren',
    loadChildren: () => import('./modules/auth/auth.module').then((m) => m.AuthModule),
    
  },
  { 
    path: '',
    title:  'overview',
    loadChildren: () => import('./overview/overview.module').then((m) => m.OverviewModule),
    canActivate:[authGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

@Injectable({providedIn: 'root'})
export class TemplatePageTitleStrategy extends TitleStrategy {
  constructor(private readonly title: Title) {
    super();
  }

  override updateTitle(routerState: RouterStateSnapshot) {
    const title = this.buildTitle(routerState);
    if (title !== undefined) {
      this.title.setTitle(`Finanzverwaltung | ${title}`);
    }
  }
}
