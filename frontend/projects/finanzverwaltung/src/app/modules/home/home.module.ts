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
import {  MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { MatTable, MatTableDataSource, MatTableModule} from '@angular/material/table';

import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import {DatePipe} from '@angular/common';
import {MatListModule} from '@angular/material/list';
import { PdfDialogComponent } from '../../extras/pdf-dialog/pdf-dialog.component';
import { PdfDialogModule } from '../../extras/pdf-dialog/pdf-dialog.module';

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
    NgApexchartsModule,
    ChartSettingsModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
    FormsModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatDividerModule,
    MatIconModule,
    DatePipe,
    MatListModule,
    MatTableModule,
    PdfDialogModule
  ]
})
export class HomeModule { }
