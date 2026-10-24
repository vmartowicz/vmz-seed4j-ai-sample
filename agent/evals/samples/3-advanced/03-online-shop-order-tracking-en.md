### Example 7 - Advanced level
```
I want to build an order tracking system for my online shop.

Q: What kind of products is it for?
A: Fashion clothing and accessories

Q: What are the steps of an order?
A:
1. The customer places an order (cart → order)
2. We prepare the order (1-2 days)
3. We ship it (carrier)
4. Customer receives it
5. Customer can return it within 14 days

Q: What are your pricing rules?
A:
- Free shipping if order > €50
- Otherwise €5 shipping fee
- Returns always free
- Loyalty discount: -10% from 5 orders

Q: How do you manage stock?
A:
- Check availability before validating the order
- Reserve the stock as soon as the order is validated
- Release it on cancellation or return
- Stock alert if < 5 units

Q: Order statuses?
A:
- AWAITING_PAYMENT
- PAID
- IN_PREPARATION
- SHIPPED
- DELIVERED
- RETURNED
- CANCELLED

Q: Cancellation rules?
A:
- Free if not shipped yet
- Impossible if already shipped (make a return)
- Refund within 5 days

Q: Which operations do you want?
A:
- Customer: place an order, pay, track their order, cancel, request a return
- Shop: prepare an order, ship it with the carrier's tracking number, confirm delivery, receive a return and refund
- Stock: restock a product, see the products under stock alert

Generate the complete domain model for me with this information.
```
