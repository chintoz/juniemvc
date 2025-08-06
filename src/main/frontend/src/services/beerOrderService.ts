import apiClient from './api';
import type { Beer } from './beerService';
import type { Customer } from './customerService';

// These interfaces would normally be generated from OpenAPI spec
// For now, we'll define them manually
interface BeerOrderLine {
  id: number;
  beer: Beer;
  orderQuantity: number;
  quantityAllocated: number;
  version: number;
  createdDate: string;
  updateDate: string;
}

interface BeerOrder {
  id: number;
  customer: Customer;
  orderStatus: OrderStatus;
  orderLines: BeerOrderLine[];
  version: number;
  createdDate: string;
  updateDate: string;
}

type OrderStatus = 
  | 'NEW'
  | 'PENDING'
  | 'READY'
  | 'PICKED_UP'
  | 'DELIVERED'
  | 'CANCELLED';

interface PagedBeerOrderResponse {
  content: BeerOrder[];
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

interface BeerOrderCreate {
  customerId: number;
  orderLines: {
    beerId: number;
    orderQuantity: number;
  }[];
}

interface BeerOrderUpdate {
  orderStatus?: OrderStatus;
  orderLines?: {
    id?: number;
    beerId: number;
    orderQuantity: number;
  }[];
}

export const beerOrderService = {
  // Get all beer orders with optional filtering and pagination
  getBeerOrders: async (
    page = 0,
    size = 20,
    customerId?: number,
    orderStatus?: OrderStatus,
    sortField = 'id',
    sortDirection = 'ASC'
  ): Promise<PagedBeerOrderResponse> => {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', size.toString());
    params.append('sortField', sortField);
    params.append('sortDirection', sortDirection);
    
    if (customerId) params.append('customerId', customerId.toString());
    if (orderStatus) params.append('orderStatus', orderStatus);
    
    const response = await apiClient.get<PagedBeerOrderResponse>('/beer-orders', { params });
    return response.data;
  },
  
  // Get a beer order by ID
  getBeerOrderById: async (orderId: number): Promise<BeerOrder> => {
    const response = await apiClient.get<BeerOrder>(`/beer-orders/${orderId}`);
    return response.data;
  },
  
  // Create a new beer order
  createBeerOrder: async (beerOrder: BeerOrderCreate): Promise<BeerOrder> => {
    const response = await apiClient.post<BeerOrder>('/beer-orders', beerOrder);
    return response.data;
  },
  
  // Update a beer order
  updateBeerOrder: async (orderId: number, beerOrder: BeerOrderUpdate): Promise<BeerOrder> => {
    const response = await apiClient.put<BeerOrder>(`/beer-orders/${orderId}`, beerOrder);
    return response.data;
  },
  
  // Update beer order status
  updateBeerOrderStatus: async (orderId: number, orderStatus: OrderStatus): Promise<BeerOrder> => {
    const response = await apiClient.patch<BeerOrder>(`/beer-orders/${orderId}/status`, { orderStatus });
    return response.data;
  },
  
  // Cancel a beer order
  cancelBeerOrder: async (orderId: number): Promise<BeerOrder> => {
    const response = await apiClient.patch<BeerOrder>(`/beer-orders/${orderId}/cancel`, {});
    return response.data;
  },
  
  // Delete a beer order
  deleteBeerOrder: async (orderId: number): Promise<void> => {
    await apiClient.delete(`/beer-orders/${orderId}`);
  }
};

export type { BeerOrder, BeerOrderLine, OrderStatus, PagedBeerOrderResponse, BeerOrderCreate, BeerOrderUpdate };