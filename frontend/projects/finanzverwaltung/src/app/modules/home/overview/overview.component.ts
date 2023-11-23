import { AuthService } from '../../../core/services/auth.service';
import { RegisterComponent } from '../../auth/register/register.component';
import { LoginComponent } from '../../auth/login/login.component';

import { Component, ViewChild } from "@angular/core";
import * as ApexCharts from 'apexcharts';
import {
  ChartComponent,
  ApexAxisChartSeries,
  ApexChart,
  ApexXAxis,
  ApexDataLabels,
  ApexTitleSubtitle,
  ApexStroke,
  ApexGrid,
  ApexPlotOptions,
  ApexYAxis,
  ApexFill
} from "ng-apexcharts";
import { ChartSettingsModule } from '../../../core/chartSettings/chartSettings.module';
import { RequestService } from '../../../core/services/request.service';


export type ChartData = {
  series: ApexAxisChartSeries;
  donut_data: ApexNonAxisChartSeries;
  xaxis: ApexXAxis;
  grid: ApexGrid;
  title: ApexTitleSubtitle;
  chart: ApexChart;
  plotOptions: ApexPlotOptions;
  dataLabels: ApexDataLabels;
  fill: ApexFill;
  yaxis: ApexYAxis;

};


@Component({
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})

export class OverviewComponent {

  @ViewChild("chart") chart!: ChartComponent;
  public lineChartData: Partial<ChartData>;
  public columnChartData: Partial<ChartData>;
  public balkenChartData: Partial<ChartData>;

  username = this.auth.getUsername();
  startBalance = 0;
  endBalance = 0;

  getStartDate() {
    let startDate = new Date();
    startDate.setMonth(startDate.getMonth() - 1);
    startDate.setDate(1);
    return startDate;
  }

  getEndDate() {
    let endDate = new Date();
    endDate.setDate(0);
    return endDate;
  }

  loadData() {
    this.requestService.post("bank-account/overview", {
      "id": "all",
      "start": 1696111200000,//this.getStartDate().getTime(),
      "end": this.getEndDate().getTime(),
    }).subscribe(response => {
      this.updateData(response);

    });
  }

  updateData(data: any) {
    var balance = data.startBalance;
    this.startBalance = data.startBalance;
    this.endBalance = data.endBalance;
    const startDate = this.getStartDate();

    this.lineChartData.title = { text: "Einnahmen/Ausgaben Übersicht für: " + startDate.toLocaleString('de-DE', { month: 'long' }) };
    this.lineChartData.series = [{
      name: "Einnahmen/Ausgaben",
      data: data.dailyExpenses.map((transaction: any) => {
        balance += transaction.amount;
        return balance.toFixed(2);
      }),
    }];

    this.lineChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    }

    this.columnChartData.title = {
      text: "Einnahmen/Ausgaben Übersicht für: " + startDate.toLocaleString('de-DE', { month: 'long' }),
    }

    this.columnChartData.series = [{
      name: "Einnahmen/Ausgaben",
      data: data.dailyExpenses.map((transaction: any) => transaction.amount.toFixed(2)),
    }];
    this.columnChartData.xaxis = {
      categories: data.dailyExpenses.map((transaction: any) => new Date(transaction.date).getDate().toString() + "."),
    }
    this.balkenChartData.series = [{
      name: "basic",
      data: data.categoryExpenses.map((category: any) => category.amount.toFixed(2))
    }];

    this.balkenChartData.xaxis = {
      categories: data.categoryExpenses.map((category: any) => {
        var categoryName = category.category?.name;
        if (categoryName == null) {
          return "Nicht Kategorisiert";
        }
        return categoryName;
      })
    };
  }

  constructor(protected chartSettings: ChartSettingsModule, private requestService: RequestService, private auth: AuthService) {

    this.lineChartData = {};

    this.columnChartData = {
      chart: {
        height: 280,
        type: "bar",
        zoom: {
          enabled: false
        }
      },
      title: {
        floating: false,
        offsetY: 20,
        align: "center",
        style: {
          color: "#444"
        }
      },
      plotOptions: {
        bar: {
          dataLabels: {
            position: "top"
          }
        }
      },
      dataLabels: {
        enabled: true,
        offsetY: -20,
        style: {
          fontSize: "12px",
          colors: ["#212121"]
        }
      },
      fill: {
        colors: [function ({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
          if (value < 0) {
            return '#fd0606'
          } else {
            return '#06fd1b'
          }
        }]
      },
      yaxis: {
        axisBorder: {
          show: false
        },
        axisTicks: {
          show: false
        },
        labels: {
          show: false,
        }
      },
    };

    this.balkenChartData = {
      chart: {
        type: "bar",
        height: 350
      },
      plotOptions: {
        bar: {
          horizontal: true
        }
      },
      dataLabels: {
        enabled: false
      },
      fill: {
        colors: [function ({ value, seriesIndex, w }: { value: number, seriesIndex: number, w: any }) {
          if (value < 0) {
            return '#fd0606'
          } else {
            return '#06fd1b'
          }
        }]
      },
    };
    this.loadData();
  }
}
