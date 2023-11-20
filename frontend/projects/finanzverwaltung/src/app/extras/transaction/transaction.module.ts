import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionComponent } from './transaction.component';
import {MatTableModule} from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator';


@NgModule({
  declarations: [TransactionComponent],
  imports: [
    CommonModule,
    MatTableModule,
    MatInputModule,
    BrowserAnimationsModule,
    MatPaginatorModule
  ]
})
export class TransactionModule { }
