import { AfterViewInit, OnInit, ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PdfDialogComponent } from '../../../extras/pdf-dialog/pdf-dialog.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { TransactionComponent } from '../../../extras/transaction/transaction.component';
import { RequestService } from '../../../core/services/request.service';
import { BankAccount, BankAccountService } from '../../../core/services/bankAccountService';

export interface BankStatement {
  transactions: Transaction[];
  issuedDate: Date;
  oldBalance: number;
  newBalance: number;
  id: number;
}
export interface Transaction {
  amount: number;
  date: Date;
  desc: string;
  title: string;
  category: Category;
}
export interface Category {
  name: string;
}

@Component({
  selector: 'statements',
  templateUrl: './statements.component.html',
  styleUrls: ['./statements.component.scss']
})
export class StatementsComponent implements AfterViewInit, OnInit {
  protected displayedColumns: string[] = ["id", "issuedDate", "oldBalance", "newBalance", "button", "pdf"];
  protected dataSource!: MatTableDataSource<BankStatement>;

  protected currentBankAccount: string = "all";
  protected bankAccounts?: BankAccount[];

  @ViewChild(MatPaginator)
  protected paginator!: MatPaginator;

  constructor(private requestService: RequestService, public dialog: MatDialog, private bankAccountService: BankAccountService) { }

  ngAfterViewInit() {
    if (this.dataSource != undefined) {
      this.dataSource.data = [];
    }

    this.dataSource = new MatTableDataSource<any>();
    this.dataSource.paginator = this.paginator;
    this.loadTableData();
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

  openPdf(id: number) {
    this.requestService.get("bank-statement/get-pdf?id=" + id, {
      observe: 'response',
      responseType: 'blob'
    }).subscribe((resp: any) => {
      const blob = new Blob([resp.body], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      window.open(url, '_blank');
    });
  }

  openUploadDialog() {
    this.dialog.open(PdfDialogComponent)
  }

  openTransactionDialog(element: BankStatement) {
    this.dialog.open(TransactionComponent, {
      width: '100%',
      height: '75%',
      data: element.transactions,
    })
  }

  loadTableData() {
    this.requestService.post("bank-statement/query-statements", {
      iban: this.currentBankAccount,
    }).subscribe(data => {
      console.log(data);
      this.dataSource.data = data;
    })
  }

  changeBankAccount(bankAccount: string) {
    this.bankAccountService.changeBankAccount(bankAccount);
  }
}
