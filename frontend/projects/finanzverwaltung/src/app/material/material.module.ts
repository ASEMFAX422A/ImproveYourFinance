import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import {NgIf} from '@angular/common';

import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatDialogModule} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';


import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatToolbarModule } from '@angular/material/toolbar';

const MaterialComponent = [
  MatButtonModule,
  MatToolbarModule,
  MatMenuModule,
  MatIconModule,
  MatSidenavModule,
  NgIf,
  MatDialogModule,
  MatFormFieldModule,
  FormsModule,
  MatInputModule,
  MatTableModule,
  ReactiveFormsModule,
  MatPaginatorModule,
  MatDividerModule,
  MatIconModule,
  DatePipe,
  MatListModule,
  MatDatepickerModule,
  MatNativeDateModule,
];

@NgModule({

  imports: [CommonModule,MaterialComponent],
  exports:[MaterialComponent]
})
export class MaterialModule { }
