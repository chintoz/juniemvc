import { render, screen, fireEvent, act } from '@testing-library/react';
import { BeerList } from './BeerList';
import { useBeers } from '../../hooks/useBeer';

// Mock the api module
jest.mock('../../services/api');

// Mock the custom hook
jest.mock('../../hooks/useBeer');

describe('BeerList', () => {
  // Mock handlers
  const mockViewBeer = jest.fn();
  const mockEditBeer = jest.fn();
  const mockCreateBeer = jest.fn();
  
  // Mock hook implementation
  const mockUpdateFilter = jest.fn();
  
  beforeEach(() => {
    jest.clearAllMocks();
    
    // Default mock implementation for the useBeers hook
    (useBeers as jest.Mock).mockReturnValue({
      beers: {
        content: [
          {
            id: 1,
            beerName: 'Test Beer',
            beerStyle: 'IPA',
            price: 9.99,
            quantityOnHand: 100,
            upc: '123456789',
            version: 1,
            createdDate: '2025-08-06T00:00:00Z',
            updateDate: '2025-08-06T00:00:00Z',
          },
          {
            id: 2,
            beerName: 'Another Beer',
            beerStyle: 'STOUT',
            price: 8.99,
            quantityOnHand: 50,
            upc: '987654321',
            version: 1,
            createdDate: '2025-08-06T00:00:00Z',
            updateDate: '2025-08-06T00:00:00Z',
          },
        ],
        pageable: {
          offset: 0,
          pageNumber: 0,
          pageSize: 20,
        },
        totalElements: 2,
        totalPages: 1,
        last: true,
        first: true,
        size: 20,
        number: 0,
        sort: {
          sorted: true,
          unsorted: false,
          empty: false,
        },
        numberOfElements: 2,
        empty: false,
      },
      loading: false,
      error: null,
      filter: {
        page: 0,
        size: 20,
      },
      updateFilter: mockUpdateFilter,
    });
  });

  it('renders the beer list', async () => {
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    // Check if the title is rendered
    expect(screen.getByText('Beers')).toBeInTheDocument();
    
    // Check if the beers are rendered
    expect(screen.getByText('Test Beer')).toBeInTheDocument();
    expect(screen.getByText('Another Beer')).toBeInTheDocument();
    
    // Check if the price is formatted correctly
    expect(screen.getByText('$9.99')).toBeInTheDocument();
    expect(screen.getByText('$8.99')).toBeInTheDocument();
  });

  it('shows loading state', async () => {
    // Override the mock to show loading state
    (useBeers as jest.Mock).mockReturnValue({
      beers: null,
      loading: true,
      error: null,
      filter: {
        page: 0,
        size: 20,
      },
      updateFilter: mockUpdateFilter,
    });
    
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('shows error state', async () => {
    // Override the mock to show error state
    (useBeers as jest.Mock).mockReturnValue({
      beers: null,
      loading: false,
      error: new Error('Failed to fetch beers'),
      filter: {
        page: 0,
        size: 20,
      },
      updateFilter: mockUpdateFilter,
    });
    
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    expect(screen.getByText('Error Loading Beers')).toBeInTheDocument();
    expect(screen.getByText('Failed to fetch beers')).toBeInTheDocument();
  });

  it('calls onViewBeer when View button is clicked', async () => {
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    // Find all View buttons and click the first one
    const viewButtons = screen.getAllByText('View');
    await act(async () => {
      fireEvent.click(viewButtons[0]);
    });
    
    // Check if the handler was called with the correct beer ID
    expect(mockViewBeer).toHaveBeenCalledWith(1);
  });

  it('calls onEditBeer when Edit button is clicked', async () => {
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    // Find all Edit buttons and click the first one
    const editButtons = screen.getAllByText('Edit');
    await act(async () => {
      fireEvent.click(editButtons[0]);
    });
    
    // Check if the handler was called with the correct beer ID
    expect(mockEditBeer).toHaveBeenCalledWith(1);
  });

  it('calls onCreateBeer when Add New Beer button is clicked', async () => {
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    // Find the Add New Beer button and click it
    const addButton = screen.getByText('Add New Beer');
    await act(async () => {
      fireEvent.click(addButton);
    });
    
    // Check if the handler was called
    expect(mockCreateBeer).toHaveBeenCalled();
  });

  it('filters beers when search is performed', async () => {
    await act(async () => {
      render(
        <BeerList
          onViewBeer={mockViewBeer}
          onEditBeer={mockEditBeer}
          onCreateBeer={mockCreateBeer}
        />
      );
    });
    
    // Find the name input and enter a value
    const nameInput = screen.getByPlaceholderText('Filter by name');
    await act(async () => {
      fireEvent.change(nameInput, { target: { value: 'Test' } });
    });
    
    // Click the Search button
    const searchButton = screen.getByText('Search');
    await act(async () => {
      fireEvent.click(searchButton);
    });
    
    // Check if updateFilter was called with the correct parameters
    expect(mockUpdateFilter).toHaveBeenCalledWith({
      beerName: 'Test',
      beerStyle: undefined,
    });
  });
});