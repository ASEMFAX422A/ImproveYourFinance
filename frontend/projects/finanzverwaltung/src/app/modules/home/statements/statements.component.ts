import { HttpClient } from '@angular/common/http'; import { AfterViewInit, ViewChild } from '@angular/core';

import { Component } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OverviewComponent } from '../overview/overview.component';
import { AnalyticsComponent } from '../analytics/analytics.component';
import { LandingPageComponent } from '../../landing-page/landing-page.component';
import { LoginComponent } from '../../auth/login/login.component';
import { PdfDialogComponent } from '../../../extras/pdf-dialog/pdf-dialog.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TransactionDialogComponent } from '../../../extras/transaction-dialog/transaction-dialog.component';
import { ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss']
})


export class StatementsComponent implements AfterViewInit {

  pdfview = true;

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol', 'button'];
  dataSource!: MatTableDataSource<PeriodicElement>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  ngAfterViewInit() {
    if(this.dataSource != undefined){
      this.dataSource.data = [];
    }
    
    this.dataSource = new MatTableDataSource<any>();
    this.dataSource.paginator = this.paginator;
    this.loadTableData();
    }

  constructor(private httpClient: HttpClient, public dialog: MatDialog, private cdr : ChangeDetectorRef) { }

  openPdfDialog() {
    this.dialog.open(PdfDialogComponent)
  }
  openTransactionDialog() {
    console.log("hashd");
    this.dialog.open(TransactionDialogComponent)
  }
  PdfSwitch() {

  
    this.pdfview = !this.pdfview;
    this.loadTableData();

  }

  loadTableData() {
    this.dataSource.data = [];
    this.cdr.detectChanges();
    this.dataSource.data = ELEMENT_DATA;
    this.dataSource.paginator = this.paginator;
  }
}


export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },



];

