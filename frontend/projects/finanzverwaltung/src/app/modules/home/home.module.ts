import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AnalyticsComponent } from './analytics/analytics.component';
import { StatementsComponent } from './statements/statements.component';
import { OverviewComponent } from './overview/overview.component';

import { MaterialModule } from '../../material/material.module';
import { SideBarComponent } from '../../extras/side-bar/side-bar.component';
import { NgApexchartsModule } from 'ng-apexcharts';
import { ChartSettingsModule } from '../../core/chartSettings/chartSettings.module';

import { PdfDialogComponent } from '../../extras/pdf-dialog/pdf-dialog.component';
import { PdfDialogModule } from '../../extras/pdf-dialog/pdf-dialog.module';
import { FooterComponent } from '../../extras/footer/footer.component';
import { ToastrModule } from 'ngx-toastr';

const routes: Routes = [
  {path: 'analytics', component: AnalyticsComponent,},
  {path: 'overview', component: OverviewComponent,},
  {path: 'statements', component: StatementsComponent,},
  
]
@NgModule({
  declarations: [
    AnalyticsComponent,
    OverviewComponent,
    StatementsComponent,
  ],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule.forChild(routes),
    SideBarComponent,
    FooterComponent,
    NgApexchartsModule,
    ChartSettingsModule,
    PdfDialogModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      preventDuplicates: true,
      positionClass: 'toast-top-right',
      enableHtml: true
    })
  ]
})
export class HomeModule { }
