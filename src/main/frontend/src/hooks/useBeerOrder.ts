import { useState, useEffect, useCallback } from 'react';
import { beerOrderService } from '../services/beerOrderService';
import type { 
  BeerOrder, 
  PagedBeerOrderResponse, 
  BeerOrderCreate,
  BeerOrderUpdate,
  OrderStatus
} from '../services/beerOrderService';

interface BeerOrderFilter {
  customerId?: number;
  orderStatus?: OrderStatus;
  page?: number;
  size?: number;
  sortField?: string;
  sortDirection?: 'ASC' | 'DESC';
}

export function useBeerOrders(initialFilter: BeerOrderFilter = {}) {
  const [beerOrders, setBeerOrders] = useState<PagedBeerOrderResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);
  const [filter, setFilter] = useState<BeerOrderFilter>({
    page: 0,
    size: 20,
    sortField: 'id',
    sortDirection: 'ASC',
    ...initialFilter
  });

  const fetchBeerOrders = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await beerOrderService.getBeerOrders(
        filter.page ?? 0,
        filter.size ?? 20,
        filter.customerId,
        filter.orderStatus,
        filter.sortField,
        filter.sortDirection
      );
      setBeerOrders(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
    } finally {
      setLoading(false);
    }
  }, [filter]);

  useEffect(() => {
    fetchBeerOrders();
  }, [fetchBeerOrders]);

  const updateFilter = useCallback((newFilter: Partial<BeerOrderFilter>) => {
    setFilter((prev) => {
      // If we're changing anything other than page, reset to first page
      if (Object.keys(newFilter).some(key => key !== 'page')) {
        return { ...prev, ...newFilter, page: 0 };
      }
      return { ...prev, ...newFilter };
    });
  }, []);

  const createBeerOrder = useCallback(async (beerOrder: BeerOrderCreate) => {
    try {
      setLoading(true);
      setError(null);
      const data = await beerOrderService.createBeerOrder(beerOrder);
      // Refresh the list after creating a new order
      fetchBeerOrders();
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [fetchBeerOrders]);

  return {
    beerOrders,
    loading,
    error,
    filter,
    updateFilter,
    createBeerOrder,
    refetch: fetchBeerOrders,
  };
}

export function useBeerOrder(orderId: number | null) {
  const [beerOrder, setBeerOrder] = useState<BeerOrder | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<Error | null>(null);

  const fetchBeerOrder = useCallback(async () => {
    if (orderId === null) return;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerOrderService.getBeerOrderById(orderId);
      setBeerOrder(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
    } finally {
      setLoading(false);
    }
  }, [orderId]);

  useEffect(() => {
    fetchBeerOrder();
  }, [fetchBeerOrder]);

  const updateBeerOrder = useCallback(async (updatedBeerOrder: BeerOrderUpdate) => {
    if (!orderId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerOrderService.updateBeerOrder(orderId, updatedBeerOrder);
      setBeerOrder(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [orderId]);

  const updateOrderStatus = useCallback(async (orderStatus: OrderStatus) => {
    if (!orderId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerOrderService.updateBeerOrderStatus(orderId, orderStatus);
      setBeerOrder(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [orderId]);

  const cancelOrder = useCallback(async () => {
    if (!orderId) return null;
    
    try {
      setLoading(true);
      setError(null);
      const data = await beerOrderService.cancelBeerOrder(orderId);
      setBeerOrder(data);
      return data;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return null;
    } finally {
      setLoading(false);
    }
  }, [orderId]);

  const deleteBeerOrder = useCallback(async () => {
    if (!orderId) return false;
    
    try {
      setLoading(true);
      setError(null);
      await beerOrderService.deleteBeerOrder(orderId);
      setBeerOrder(null);
      return true;
    } catch (err) {
      setError(err instanceof Error ? err : new Error('An unknown error occurred'));
      return false;
    } finally {
      setLoading(false);
    }
  }, [orderId]);

  return {
    beerOrder,
    loading,
    error,
    updateBeerOrder,
    updateOrderStatus,
    cancelOrder,
    deleteBeerOrder,
    refetch: fetchBeerOrder,
  };
}