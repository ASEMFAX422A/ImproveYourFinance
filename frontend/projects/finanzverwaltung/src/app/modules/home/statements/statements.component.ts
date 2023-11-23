import { AfterViewInit, ViewChild } from '@angular/core';

import { Component } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OverviewComponent } from '../overview/overview.component';
import { AnalyticsComponent } from '../analytics/analytics.component';
import { LandingPageComponent } from '../../landing-page/landing-page.component';
import { LoginComponent } from '../../auth/login/login.component';
import { PdfDialogComponent } from '../../../extras/pdf-dialog/pdf-dialog.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ChangeDetectorRef } from '@angular/core';
import { TransactionComponent } from '../../../extras/transaction/transaction.component';
import { RequestService } from '../../../core/services/request.service';
import { BankAccount, BankAccountService } from '../../../core/services/bankAccountService';

export interface BankStatement {
  transactions: Transaction[];
  issuedDate: Date;
  oldBalance: number;
  newBalance: number;
  id:number;
}
export interface Transaction{
  amount:number;
  date:Date;
  desc:string;
  title:string;
  category:Category;
}
export interface Category{
  name:string;
}
@Component({
  selector: 'statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss']
})
export class StatementsComponent implements AfterViewInit {

  public bankAccounts?: BankAccount[];
  public currentBankAccount: string = "all";
  displayedColumns: string[] = ["id","issuedDate","oldBalance","newBalance","button","pdf"];
  dataSource!: MatTableDataSource<BankStatement>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  ngAfterViewInit() {
    if (this.dataSource != undefined) {
      this.dataSource.data = [];
    }

    this.dataSource = new MatTableDataSource<any>();
    this.dataSource.paginator = this.paginator;
    this.loadTableData();
  }

  constructor(private requestService: RequestService, public dialog: MatDialog, private bankAccountService: BankAccountService ) {}
  openPdf(id:number){
    this.requestService.get("bank-statement/get-pdf?id="+id, {
      observe: 'response',
      responseType: 'blob'
    }).subscribe((resp: any) => {
      console.log(resp);
      const blob = new Blob([resp.body], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      window.open(url, '_blank');
    });
  }
  
  openPdfDialog() {
    this.dialog.open(PdfDialogComponent)
  }
  openTransactionDialog(element: BankStatement) {
    const dialogRef = this.dialog.open(TransactionComponent, {
      width: '100%',
      height: '75%',
      data: element.transactions,
    })
  }


  loadTableData() {
    this.requestService.post("bank-statement/query-statements",{
      iban: this.currentBankAccount,
    }).subscribe(data=>{
      console.log(data);
      this.dataSource.data=data;
    })
  }
  
  changeBankAccount(bankAccount:string){
    this.bankAccountService.changeBankAccount(bankAccount);
  }
  ngOnInit() {
    this.bankAccountService.currentBankAccount$.subscribe((chosenBankAccount) => {
      this.currentBankAccount = chosenBankAccount;
      this.loadTableData();
    });
    
    this.bankAccountService.bankAccounts$.subscribe((bankAccounts) => {
      this.bankAccounts = bankAccounts;
    });
  }
}


