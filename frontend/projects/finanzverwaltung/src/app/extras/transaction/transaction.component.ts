import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Transaction } from '../../modules/home/statements/statements.component';


@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.scss']
})

export class TransactionComponent {
  displayedColumns: string[] = ["date","amount","title","category","desc"];

  dataSource = new MatTableDataSource<Transaction>();
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.dataSource.data = data;
    console.log('Übergebene Daten:', data);
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

}
