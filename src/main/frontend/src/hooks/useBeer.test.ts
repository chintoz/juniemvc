import { renderHook, act } from '@testing-library/react';
import { useBeers, useBeer } from './useBeer';
import { beerService } from '../services/beerService';

// Mock the api module
jest.mock('../services/api');

// Mock the beer service
jest.mock('../services/beerService');

describe('useBeers hook', () => {
  const mockBeersResponse = {
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
    ],
    pageable: {
      offset: 0,
      pageNumber: 0,
      pageSize: 20,
    },
    totalElements: 1,
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
    numberOfElements: 1,
    empty: false,
  };

  beforeEach(() => {
    jest.clearAllMocks();
    (beerService.getBeers as jest.Mock).mockResolvedValue(mockBeersResponse);
  });

  it('should fetch beers on initial render', async () => {
    const { result } = renderHook(() => useBeers());
    
    // Initial state should be loading
    expect(result.current.loading).toBe(true);
    expect(result.current.beers).toBe(null);
    
    // Wait for the async operation to complete
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After loading, we should have data
    expect(result.current.loading).toBe(false);
    expect(result.current.beers).toEqual(mockBeersResponse);
    expect(result.current.error).toBe(null);
    
    // Check if the service was called with the default parameters
    expect(beerService.getBeers).toHaveBeenCalledWith(
      0, 20, undefined, undefined, 'id', 'ASC'
    );
  });

  it('should update filter and refetch beers', async () => {
    const { result } = renderHook(() => useBeers());
    
    // Wait for the initial data load
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After initial load
    expect(result.current.loading).toBe(false);
    
    // Update the filter
    act(() => {
      result.current.updateFilter({
        beerName: 'Test',
        beerStyle: 'IPA',
      });
    });
    
    // Should be loading again
    expect(result.current.loading).toBe(true);
    
    // Wait for the data to load with new filter
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After filter update and data load
    expect(result.current.loading).toBe(false);
    
    // Check if the service was called with the updated parameters
    expect(beerService.getBeers).toHaveBeenCalledWith(
      0, 20, 'Test', 'IPA', 'id', 'ASC'
    );
  });

  it('should handle errors', async () => {
    // Mock an error response
    const error = new Error('Failed to fetch beers');
    (beerService.getBeers as jest.Mock).mockRejectedValue(error);
    
    const { result } = renderHook(() => useBeers());
    
    // Wait for the error to be caught
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // Should have error state
    expect(result.current.loading).toBe(false);
    expect(result.current.beers).toBe(null);
    expect(result.current.error).toEqual(error);
  });
});

describe('useBeer hook', () => {
  const mockBeer = {
    id: 1,
    beerName: 'Test Beer',
    beerStyle: 'IPA',
    price: 9.99,
    quantityOnHand: 100,
    upc: '123456789',
    version: 1,
    createdDate: '2025-08-06T00:00:00Z',
    updateDate: '2025-08-06T00:00:00Z',
  };

  beforeEach(() => {
    jest.clearAllMocks();
    (beerService.getBeerById as jest.Mock).mockResolvedValue(mockBeer);
    (beerService.updateBeer as jest.Mock).mockResolvedValue({
      ...mockBeer,
      version: 2,
      updateDate: '2025-08-06T01:00:00Z',
    });
    (beerService.patchBeer as jest.Mock).mockResolvedValue({
      ...mockBeer,
      beerName: 'Updated Beer',
      version: 2,
      updateDate: '2025-08-06T01:00:00Z',
    });
    (beerService.deleteBeer as jest.Mock).mockResolvedValue(undefined);
  });

  it('should fetch a beer by ID on initial render', async () => {
    const { result } = renderHook(() => useBeer(1));
    
    // Initial state - loading is initially true when fetching data
    expect(result.current.loading).toBe(true);
    expect(result.current.beer).toBe(null);
    
    // Wait for the data to load
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After loading, we should have data
    expect(result.current.loading).toBe(false);
    expect(result.current.beer).toEqual(mockBeer);
    expect(result.current.error).toBe(null);
    
    // Check if the service was called with the correct ID
    expect(beerService.getBeerById).toHaveBeenCalledWith(1);
  });

  it('should not fetch if beerId is null', () => {
    const { result } = renderHook(() => useBeer(null));
    
    // Service should not be called
    expect(beerService.getBeerById).not.toHaveBeenCalled();
    
    // State should be initialized but empty
    expect(result.current.loading).toBe(false);
    expect(result.current.beer).toBe(null);
    expect(result.current.error).toBe(null);
  });

  it('should update a beer', async () => {
    const { result } = renderHook(() => useBeer(1));
    
    // Wait for initial data load
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After initial data load
    expect(result.current.beer).toEqual(mockBeer);
    
    // Update the beer
    const updatedBeer = {
      beerName: 'Updated Beer',
      beerStyle: 'IPA',
      price: 10.99,
      quantityOnHand: 90,
      upc: '123456789',
    };
    
    // Perform the update
    await act(async () => {
      result.current.updateBeer(updatedBeer);
      // Wait for the update to complete
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // Check if the service was called with the correct parameters
    expect(beerService.updateBeer).toHaveBeenCalledWith(1, updatedBeer);
    
    // Check if the beer was updated in the state
    expect(result.current.beer).toEqual({
      ...mockBeer,
      version: 2,
      updateDate: '2025-08-06T01:00:00Z',
    });
  });

  it('should patch a beer', async () => {
    const { result } = renderHook(() => useBeer(1));
    
    // Wait for initial data load
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After initial data load
    expect(result.current.beer).toEqual(mockBeer);
    
    // Patch the beer
    const patch = {
      beerName: 'Updated Beer',
    };
    
    // Perform the patch
    await act(async () => {
      result.current.patchBeer(patch);
      // Wait for the patch to complete
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // Check if the service was called with the correct parameters
    expect(beerService.patchBeer).toHaveBeenCalledWith(1, patch);
    
    // Check if the beer was updated in the state
    expect(result.current.beer).toEqual({
      ...mockBeer,
      beerName: 'Updated Beer',
      version: 2,
      updateDate: '2025-08-06T01:00:00Z',
    });
  });

  it('should delete a beer', async () => {
    const { result } = renderHook(() => useBeer(1));
    
    // Wait for initial data load
    await act(async () => {
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // After initial data load
    expect(result.current.beer).toEqual(mockBeer);
    
    // Delete the beer
    await act(async () => {
      result.current.deleteBeer();
      // Wait for the delete to complete
      await new Promise(resolve => setTimeout(resolve, 0));
    });
    
    // Check if the service was called with the correct ID
    expect(beerService.deleteBeer).toHaveBeenCalledWith(1);
    
    // Check if the beer was removed from the state
    expect(result.current.beer).toBe(null);
  });
});