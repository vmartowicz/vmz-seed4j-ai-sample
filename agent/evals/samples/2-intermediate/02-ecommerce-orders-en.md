### Example 4 - Intermediate level
```
E-commerce order management system.

Context:
A customer places an order with several product lines.

Business rules:
- Each order has a status: PENDING, PAID, SHIPPED, DELIVERED, CANCELLED.
- An email is sent on each status change.
- Each order line has a product, a quantity and a unit price.
- The order total is the sum of the order lines.
- Each product has an available stock.
- When an order is placed, the product stock is checked and reserved.
- If the stock is insufficient, the order cannot be validated.
- Returns are possible within 14 days after delivery.
- A return updates the stock and the order status.

Required operations:
- Add a product to the catalog with its stock
- Place an order with several lines
- Pay for an order
- Ship an order
- Mark an order as delivered
- Cancel an order
- Return a delivered order
- View an order and its total
```
