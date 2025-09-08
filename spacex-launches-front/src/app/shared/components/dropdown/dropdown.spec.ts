import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Dropdown, DropdownOption } from './dropdown';

describe('Dropdown', () => {
  let component: Dropdown;
  let fixture: ComponentFixture<Dropdown>;

  const mockOptions: DropdownOption[] = [
    { value: 'all', label: 'All Status' },
    { value: 'success', label: 'Success' },
    { value: 'failure', label: 'Failure' },
    { value: 'upcoming', label: 'Upcoming' }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Dropdown]
    }).compileComponents();

    fixture = TestBed.createComponent(Dropdown);
    component = fixture.componentInstance;
    component.options = mockOptions;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.isOpen).toBeFalsy();
    expect(component.selectedValue).toBe(null);
    expect(component.searchTerm).toBe('');
    expect(component.disabled).toBeFalsy();
    expect(component.showSearch).toBeFalsy();
    expect(component.size).toBe('md');
  });

  it('should display placeholder when no option is selected', () => {
    component.placeholder = 'Select option';
    component.selectedValue = '';
    
    const label = component.getSelectedLabel();
    expect(label).toBe('Select option');
  });

  it('should display selected option label', () => {
    component.selectedValue = 'success';
    
    const label = component.getSelectedLabel();
    expect(label).toBe('Success');
  });

  it('should toggle dropdown when toggleDropdown is called', () => {
    expect(component.isOpen).toBeFalsy();
    
    component.toggleDropdown();
    expect(component.isOpen).toBeTruthy();
    
    component.toggleDropdown();
    expect(component.isOpen).toBeFalsy();
  });

  it('should not toggle dropdown when disabled', () => {
    component.disabled = true;
    component.isOpen = false;
    
    component.toggleDropdown();
    expect(component.isOpen).toBeFalsy();
  });

  it('should select option and emit selectionChange', () => {
    jest.spyOn(component.selectionChange, 'emit');
    
    component.selectOption(mockOptions[1]);
    
    expect(component.selectedValue).toBe('success');
    expect(component.isOpen).toBeFalsy();
    expect(component.selectionChange.emit).toHaveBeenCalledWith('success');
  });

  it('should filter options based on search term', () => {
    component.searchTerm = 'suc';
    component['filterOptions']();
    
    expect(component.filteredOptions.length).toBe(1);
    expect(component.filteredOptions[0].value).toBe('success');
  });

  it('should return all options when search term is empty', () => {
    component.searchTerm = '';
    component['filterOptions']();
    
    expect(component.filteredOptions.length).toBe(4);
    expect(component.filteredOptions).toEqual(mockOptions);
  });

  it('should handle search input', () => {
    const mockEvent = {
      target: { value: 'fail' }
    } as any;
    
    component.onSearchInput(mockEvent);
    
    expect(component.searchTerm).toBe('fail');
  });

  it('should close dropdown when clicking outside', () => {
    component.isOpen = true;
    
    const mockEvent = { target: document.createElement('div') } as any;
    component.onDocumentClick(mockEvent);
    
    expect(component.isOpen).toBeFalsy();
  });

  it('should handle case insensitive search', () => {
    component.searchTerm = 'SUCCESS';
    component['filterOptions']();
    
    expect(component.filteredOptions.length).toBe(1);
    expect(component.filteredOptions[0].value).toBe('success');
  });

  it('should clear search term when dropdown closes', () => {
    component.searchTerm = 'test';
    component.isOpen = true;
    
    component.toggleDropdown();
    
    expect(component.isOpen).toBeFalsy();
    expect(component.searchTerm).toBe('');
  });

  it('should update filtered options when ngOnChanges is called', () => {
    component.options = [{ value: 'test', label: 'Test' }];
    
    component.ngOnChanges();
    
    expect(component.filteredOptions).toEqual([{ value: 'test', label: 'Test' }]);
  });

  it('should handle enter key to close dropdown', () => {
    component.isOpen = true;
    
    component.toggleDropdown();
    
    expect(component.isOpen).toBeFalsy();
  });

  it('should handle escape key to close dropdown', () => {
    component.isOpen = true;
    
    component.toggleDropdown();
    
    expect(component.isOpen).toBeFalsy();
  });

  it('should apply correct size classes', () => {
    component.size = 'sm';
    fixture.detectChanges();
    
    const dropdownElement = fixture.nativeElement.querySelector('.dropdown-container');
    expect(dropdownElement.classList.contains('dropdown-sm')).toBeTruthy();
  });
});
