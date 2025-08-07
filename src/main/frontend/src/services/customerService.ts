import apiClient from './api';

// These interfaces would normally be generated from OpenAPI spec
// For now, we'll define them manually
interface Customer {
  id: number;
  customerName: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  version: number;
  createdDate: string;
  updateDate: string;
}

interface PagedCustomerResponse {
  content: Customer[];
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

interface CustomerPatch {
  customerName?: string;
  email?: string;
  phone?: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
}

export const customerService = {
  // Get all customers with optional filtering and pagination
  getCustomers: async (
    page = 0,
    size = 20,
    customerName?: string,
    sortField = 'id',
    sortDirection = 'ASC'
  ): Promise<PagedCustomerResponse> => {
    const params = new URLSearchParams();
    params.append('page', page.toString());
    params.append('size', size.toString());
    params.append('sortField', sortField);
    params.append('sortDirection', sortDirection);
    
    if (customerName) params.append('customerName', customerName);
    
    const response = await apiClient.get<PagedCustomerResponse>('/customers', { params });
    return response.data;
  },
  
  // Get a customer by ID
  getCustomerById: async (customerId: number): Promise<Customer> => {
    const response = await apiClient.get<Customer>(`/customers/${customerId}`);
    return response.data;
  },
  
  // Create a new customer
  createCustomer: async (customer: Omit<Customer, 'id' | 'version' | 'createdDate' | 'updateDate'>): Promise<Customer> => {
    const response = await apiClient.post<Customer>('/customers', customer);
    return response.data;
  },
  
  // Update a customer
  updateCustomer: async (customerId: number, customer: Omit<Customer, 'id' | 'version' | 'createdDate' | 'updateDate'>): Promise<Customer> => {
    const response = await apiClient.put<Customer>(`/customers/${customerId}`, customer);
    return response.data;
  },
  
  // Patch a customer
  patchCustomer: async (customerId: number, patch: CustomerPatch): Promise<Customer> => {
    const response = await apiClient.patch<Customer>(`/customers/${customerId}`, patch);
    return response.data;
  },
  
  // Delete a customer
  deleteCustomer: async (customerId: number): Promise<void> => {
    await apiClient.delete(`/customers/${customerId}`);
  }
};

export type { Customer, PagedCustomerResponse, CustomerPatch };