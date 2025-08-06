import { useState, useEffect } from 'react';
import { useBeer } from '../../hooks/useBeer';
import type {Beer} from '../../services/beerService';
import { Button } from '../ui/button';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '../ui/card';
import { Input } from '../ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../ui/select';
import { Label } from '../ui/label';

interface BeerFormProps {
  beerId?: number;
  onSave: () => void;
  onCancel: () => void;
}

export function BeerForm({ beerId, onSave, onCancel }: BeerFormProps) {
  const isEditMode = beerId !== undefined;
  const { beer, loading, error, updateBeer, patchBeer } = useBeer(isEditMode ? beerId : null);
  
  const [formData, setFormData] = useState<Partial<Beer>>({
    beerName: '',
    beerStyle: '',
    upc: '',
    price: 0,
    quantityOnHand: 0,
  });
  
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});
  const [isSaving, setIsSaving] = useState(false);
  const [saveError, setSaveError] = useState<string | null>(null);

  // Load beer data if in edit mode
  useEffect(() => {
    if (isEditMode && beer) {
      setFormData({
        beerName: beer.beerName,
        beerStyle: beer.beerStyle,
        upc: beer.upc,
        price: beer.price,
        quantityOnHand: beer.quantityOnHand,
      });
    }
  }, [isEditMode, beer]);

  const validateForm = (): boolean => {
    const errors: Record<string, string> = {};
    
    if (!formData.beerName?.trim()) {
      errors.beerName = 'Beer name is required';
    }
    
    if (!formData.beerStyle?.trim()) {
      errors.beerStyle = 'Beer style is required';
    }
    
    if (!formData.upc?.trim()) {
      errors.upc = 'UPC is required';
    }
    
    if (formData.price === undefined || formData.price <= 0) {
      errors.price = 'Price must be greater than 0';
    }
    
    if (formData.quantityOnHand === undefined || formData.quantityOnHand < 0) {
      errors.quantityOnHand = 'Quantity must be 0 or greater';
    }
    
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    
    // Convert numeric values
    if (name === 'price' || name === 'quantityOnHand') {
      setFormData({
        ...formData,
        [name]: parseFloat(value) || 0,
      });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }
    
    // Clear error for this field if it exists
    if (formErrors[name]) {
      setFormErrors({
        ...formErrors,
        [name]: '',
      });
    }
  };

  const handleStyleChange = (value: string) => {
    setFormData({
      ...formData,
      beerStyle: value,
    });
    
    // Clear error for this field if it exists
    if (formErrors.beerStyle) {
      setFormErrors({
        ...formErrors,
        beerStyle: '',
      });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setIsSaving(true);
    setSaveError(null);
    
    try {
      if (isEditMode && beerId) {
        // Update existing beer
        await patchBeer({
          beerName: formData.beerName,
          beerStyle: formData.beerStyle,
          upc: formData.upc,
          price: formData.price,
          quantityOnHand: formData.quantityOnHand,
        });
      } else {
        // Create new beer via service directly
        await updateBeer({
          beerName: formData.beerName!,
          beerStyle: formData.beerStyle!,
          upc: formData.upc!,
          price: formData.price!,
          quantityOnHand: formData.quantityOnHand!,
        });
      }
      
      onSave();
    } catch (err) {
      setSaveError(err instanceof Error ? err.message : 'An error occurred while saving');
    } finally {
      setIsSaving(false);
    }
  };

  if (loading && isEditMode) {
    return (
      <Card className="w-full">
        <CardHeader>
          <CardTitle>Loading Beer...</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex justify-center p-8">Loading...</div>
        </CardContent>
      </Card>
    );
  }

  if (error && isEditMode) {
    return (
      <Card className="w-full">
        <CardHeader>
          <CardTitle className="text-red-500">Error Loading Beer</CardTitle>
        </CardHeader>
        <CardContent>
          <p>{error.message}</p>
          <Button onClick={onCancel} className="mt-4">
            Back
          </Button>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="w-full">
      <CardHeader>
        <CardTitle>{isEditMode ? 'Edit Beer' : 'Create New Beer'}</CardTitle>
      </CardHeader>
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
          {saveError && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
              {saveError}
            </div>
          )}
          
          <div className="space-y-2">
            <Label htmlFor="beerName">Beer Name</Label>
            <Input
              id="beerName"
              name="beerName"
              value={formData.beerName || ''}
              onChange={handleInputChange}
              className={formErrors.beerName ? 'border-red-500' : ''}
            />
            {formErrors.beerName && (
              <p className="text-red-500 text-sm">{formErrors.beerName}</p>
            )}
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="beerStyle">Beer Style</Label>
            <Select
              value={formData.beerStyle || ''}
              onValueChange={handleStyleChange}
            >
              <SelectTrigger id="beerStyle" className={formErrors.beerStyle ? 'border-red-500' : ''}>
                <SelectValue placeholder="Select a style" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ALE">Ale</SelectItem>
                <SelectItem value="IPA">IPA</SelectItem>
                <SelectItem value="LAGER">Lager</SelectItem>
                <SelectItem value="STOUT">Stout</SelectItem>
                <SelectItem value="WHEAT">Wheat</SelectItem>
              </SelectContent>
            </Select>
            {formErrors.beerStyle && (
              <p className="text-red-500 text-sm">{formErrors.beerStyle}</p>
            )}
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="upc">UPC</Label>
            <Input
              id="upc"
              name="upc"
              value={formData.upc || ''}
              onChange={handleInputChange}
              className={formErrors.upc ? 'border-red-500' : ''}
            />
            {formErrors.upc && (
              <p className="text-red-500 text-sm">{formErrors.upc}</p>
            )}
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="price">Price</Label>
            <Input
              id="price"
              name="price"
              type="number"
              step="0.01"
              min="0"
              value={formData.price || ''}
              onChange={handleInputChange}
              className={formErrors.price ? 'border-red-500' : ''}
            />
            {formErrors.price && (
              <p className="text-red-500 text-sm">{formErrors.price}</p>
            )}
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="quantityOnHand">Quantity On Hand</Label>
            <Input
              id="quantityOnHand"
              name="quantityOnHand"
              type="number"
              min="0"
              value={formData.quantityOnHand || ''}
              onChange={handleInputChange}
              className={formErrors.quantityOnHand ? 'border-red-500' : ''}
            />
            {formErrors.quantityOnHand && (
              <p className="text-red-500 text-sm">{formErrors.quantityOnHand}</p>
            )}
          </div>
        </CardContent>
        <CardFooter className="flex justify-between">
          <Button type="button" variant="outline" onClick={onCancel}>
            Cancel
          </Button>
          <Button type="submit" disabled={isSaving}>
            {isSaving ? 'Saving...' : 'Save Beer'}
          </Button>
        </CardFooter>
      </form>
    </Card>
  );
}