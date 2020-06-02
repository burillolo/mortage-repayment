import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable} from '@angular/material/table';
import { Period } from '../period';
import { CuotasServiceService } from '../cuotas-service.service';
import { Observable } from 'rxjs';
import { TablaCuotasDataSource } from './tabla-cuotas-datasource';

@Component({
  selector: 'app-tabla-cuotas',
  templateUrl: './tabla-cuotas.component.html',
  styleUrls: ['./tabla-cuotas.component.css']
})
export class TablaCuotasComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatTable) table: MatTable<Period>;
  dataSource: TablaCuotasDataSource;
  isLoadingResults$: Observable<boolean>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'cuota', 'fechaPago', 'principal', 'interest', 'restante', 'capitalPendiente'];

  constructor(private cuotasService: CuotasServiceService) {}

  ngOnInit() {
    this.isLoadingResults$ = this.cuotasService.getIsLoadingResult();
    this.dataSource = new TablaCuotasDataSource(this.cuotasService);
  }

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }
}
