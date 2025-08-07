import apiClient from './api';

// These interfaces would normally be generated from OpenAPI spec
// For now, we'll define them manually
interface Beer {
  id: number;
  beerName: string;
  beerStyle: string;
  upc: string;
  price: number;
  quantityOnHand: number;
  version: number;
  createdDate: string;
  updateDate: string;
}

interface PagedBeerResponse {
  content: Beer[];
  pageable: {
    offset: number;
    pageNumber: number;
    pageSize: number;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}

interface BeerPatch {
  beerName?: string;
  beerStyle?: string;
  upc?: string;
  price?: number;
  quantityOnHand?: number;
}

export const beerService = {
  // Get all beers with optional filtering and pagination
  getBeers: async (
    page = 0,
    size = 20,
    beerName?: string,
    beerStyle?: string,
    sortField = 'id',
    sortDirection = 'ASC'
  ): Promise<PagedBeerResponse> => {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', size.toString());
    params.append('sortField', sortField);
    params.append('sortDirection', sortDirection);
    
    if (beerName) params.append('beerName', beerName);
    if (beerStyle) params.append('beerStyle', beerStyle);
    
    const response = await apiClient.get<PagedBeerResponse>('/beers', { params });
    return response.data;
  },
  
  // Get a beer by ID
  getBeerById: async (beerId: number): Promise<Beer> => {
    const response = await apiClient.get<Beer>(`/beers/${beerId}`);
    return response.data;
  },
  
  // Create a new beer
  createBeer: async (beer: Omit<Beer, 'id' | 'version' | 'createdDate' | 'updateDate'>): Promise<Beer> => {
    const response = await apiClient.post<Beer>('/beers', beer);
    return response.data;
  },
  
  // Update a beer
  updateBeer: async (beerId: number, beer: Omit<Beer, 'id' | 'version' | 'createdDate' | 'updateDate'>): Promise<Beer> => {
    const response = await apiClient.put<Beer>(`/beers/${beerId}`, beer);
    return response.data;
  },
  
  // Patch a beer
  patchBeer: async (beerId: number, patch: BeerPatch): Promise<Beer> => {
    const response = await apiClient.patch<Beer>(`/beers/${beerId}`, patch);
    return response.data;
  },
  
  // Delete a beer
  deleteBeer: async (beerId: number): Promise<void> => {
    await apiClient.delete(`/beers/${beerId}`);
  }
};

export type { Beer, PagedBeerResponse, BeerPatch };