import { Component, Input, Output, EventEmitter, ElementRef, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface DropdownOption {
  value: any;
  label: string;
  disabled?: boolean;
  icon?: string;
}

@Component({
  selector: 'app-dropdown',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dropdown.html',
  styleUrl: './dropdown.css'
})
export class Dropdown {
  @Input() options: DropdownOption[] = [];
  @Input() selectedValue: any = null;
  @Input() placeholder: string = 'Select option';
  @Input() disabled: boolean = false;
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() showSearch: boolean = false;
  @Output() selectionChange = new EventEmitter<any>();

  isOpen = false;
  searchTerm = '';
  filteredOptions: DropdownOption[] = [];

  constructor(private elementRef: ElementRef) {}

  ngOnInit(): void {
    this.filteredOptions = [...this.options];
  }

  ngOnChanges(): void {
    this.filteredOptions = [...this.options];
    this.filterOptions();
  }

  toggleDropdown(): void {
    if (!this.disabled) {
      this.isOpen = !this.isOpen;
      if (this.isOpen) {
        this.searchTerm = '';
        this.filteredOptions = [...this.options];
      } else {
        this.searchTerm = '';
      }
    }
  }

  selectOption(option: DropdownOption): void {
    if (!option.disabled) {
      this.selectedValue = option.value;
      this.selectionChange.emit(option.value);
      this.isOpen = false;
      this.searchTerm = '';
    }
  }

  onSearchInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchTerm = target.value;
    this.filterOptions();
  }

  private filterOptions(): void {
    if (!this.searchTerm) {
      this.filteredOptions = [...this.options];
    } else {
      this.filteredOptions = this.options.filter(option =>
        option.label.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  getSelectedLabel(): string {
    const selected = this.options.find(option => option.value === this.selectedValue);
    return selected ? selected.label : this.placeholder;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isOpen = false;
    }
  }
}
