import { useState, useEffect, useCallback } from 'react';
import { customerService } from '../services/customerService';
import type { Customer, PagedCustomerResponse, CustomerPatch } from '../services/customerService';

interface CustomerFilter {
  customerName?: string;
  page?: number;
  size?: number;
  sortField?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export function useCustomers(initialFilter: CustomerFilter = {}) {
  const [customers, setCustomers] = useState<PagedCustomerResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);
  const [filter, setFilter] = useState<CustomerFilter>({
    page: 0,
    size: 20,
    sortField: 'id',
    sortDirection: 'ASC',
    ...initialFilter
  });

  const fetchCustomers = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await customerService.getCustomers(
        filter.page ?? 0,
        filter.size ?? 20,
        filter.customerName,
        filter.sortField,
        filter.sortDirection
      );
      setCustomers(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
    } finally {
      setLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    fetchCustomers();
  }, [fetchCustomers]);

  const updateFilter = useCallback((newFilter: Partial<CustomerFilter>) => {
    setFilter((prev) => {
      // If we're changing anything other than page, reset to first page
      if (Object.keys(newFilter).some(key => key !== 'page')) {
        return { ...prev, ...newFilter, page: 0 };
      }
      return { ...prev, ...newFilter };
    });
  }, []);

  return {
    customers,
    loading,
    error,
    filter,
    updateFilter,
    refetch: fetchCustomers,
  };
}

export function useCustomer(customerId: number | null) {
  const [customer, setCustomer] = useState<Customer | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchCustomer = useCallback(async () => {
    if (customerId === null) return;
    
    try {
      setLoading(true);
      setError(null);
      const data = await customerService.getCustomerById(customerId);
      setCustomer(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
    } finally {
      setLoading(false);
    }
  }, [customerId]);

  useEffect(() => {
    fetchCustomer();
  }, [fetchCustomer]);

  const updateCustomer = useCallback(async (updatedCustomer: Omit<Customer, 'id' | 'version' | 'createdDate' | 'updateDate'>) => {
    if (!customerId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await customerService.updateCustomer(customerId, updatedCustomer);
      setCustomer(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [customerId]);

  const patchCustomer = useCallback(async (patch: CustomerPatch) => {
    if (!customerId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await customerService.patchCustomer(customerId, patch);
      setCustomer(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [customerId]);

  const deleteCustomer = useCallback(async () => {
    if (!customerId) return false;
    
    try {
      setLoading(true);
      setError(null);
      await customerService.deleteCustomer(customerId);
      setCustomer(null);
      return true;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return false;
    } finally {
      setLoading(false);
    }
  }, [customerId]);

  return {
    customer,
    loading,
    error,
    updateCustomer,
    patchCustomer,
    deleteCustomer,
    refetch: fetchCustomer,
  };
}