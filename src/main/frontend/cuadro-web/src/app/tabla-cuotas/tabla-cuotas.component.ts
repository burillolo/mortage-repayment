import { AfterViewInit, Component, OnInit, ViewChild, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable, MatTableDataSource} from '@angular/material/table';
import { Period } from '../period';
import { CuotasServiceService } from '../cuotas-service.service';
import { catchError, map, Observable, of, startWith, switchMap } from 'rxjs';
import { PageResponse } from '../page-response';
import { Settings } from '../settings';
import { LoanSummary } from '../loan-summary';

@Component({
  selector: 'app-tabla-cuotas',
  templateUrl: './tabla-cuotas.component.html',
  styleUrls: ['./tabla-cuotas.component.css']
})
export class TablaCuotasComponent implements AfterViewInit, OnInit, OnChanges {

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['mes', 'cuota', 'fechaPago', 'principal', 'interest', 'restante', 'capitalPendiente'];

  @Input() loanData: Settings | null = null;
  @Output() backToSimulator = new EventEmitter<void>();

  loanSummary: LoanSummary | null = null;
  showSummaryPanel = false;

  constructor(private cuotasService: CuotasServiceService) {}

  totalData: number = 0;
  EmpData!: Period[];

  dataSource = new MatTableDataSource<Period>();

  isLoadingResults = false;

  @ViewChild('paginator') paginator!: MatPaginator;

  pageSizes = [25, 50, 100, 250];

  // Store loan parameters
  private nominal: number = 0;
  private interest: number = 0;
  private months: number = 0;

  getTableData$(nominal: number, interest: number, months: number, pageNumber: number, pageSize: number) {
    return this.cuotasService.obtenerPeriodosCuotas(nominal, interest, months, pageNumber, pageSize);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;

    this.paginator.page
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.getTableData$(
            this.nominal,
            this.interest,
            this.months,
            this.paginator.pageIndex,
            this.paginator.pageSize
          ).pipe(catchError(() => of({
            content: [],
            totalElements: 0,
            totalPages: 0,
            number: 0,
            size: this.paginator.pageSize,
            first: true,
            last: true,
            numberOfElements: 0,
            empty: true,
            pageable: {
              sort: { sorted: false, unsorted: true, empty: true },
              pageNumber: 0,
              pageSize: this.paginator.pageSize,
              offset: 0,
              paged: true,
              unpaged: false
            },
            sort: { sorted: false, unsorted: true, empty: true }
          } as PageResponse<Period>)));
        }),
        map((pagedData: PageResponse<Period>) => {
          this.totalData = pagedData.totalElements;
          this.isLoadingResults = false;
          return pagedData.content;
        })
      )
      .subscribe((data: Period[]) => {
        this.EmpData = data;
        this.dataSource = new MatTableDataSource<Period>(this.EmpData);
      });
  }

  ngOnInit(): void {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['loanData'] && this.loanData) {
      this.loadLoanData(this.loanData.nominal, this.loanData.rate, this.loanData.duration);
      this.loadLoanSummary(this.loanData.nominal, this.loanData.rate, this.loanData.duration);
    }
  }

  /**
   * Method to load data with loan parameters - called from parent component
   */
  loadLoanData(nominal: number, interest: number, months: number): void {
    this.nominal = nominal;
    this.interest = interest;
    this.months = months;
    
    // Reset paginator and trigger data load
    if (this.paginator) {
      this.paginator.pageIndex = 0;
      // Manually trigger the page event to refresh data
      this.paginator.page.emit({
        pageIndex: 0,
        pageSize: this.paginator.pageSize,
        length: this.totalData
      });
    }
  }

  goBack(): void {
    this.backToSimulator.emit();
  }

  loadLoanSummary(nominal: number, interest: number, months: number): void {
    this.cuotasService.getLoanSummary(nominal, interest, months).subscribe({
      next: (summary) => {
        this.loanSummary = summary;
      },
      error: (error) => {
        console.error('Error loading loan summary:', error);
      }
    });
  }

  showSummary(): void {
    this.showSummaryPanel = true;
  }

  closeSummary(): void {
    this.showSummaryPanel = false;
  }
}

