package ru.university.equiprent.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {
    private final AtomicLong productSequence = new AtomicLong();
    private final AtomicLong categorySequence = new AtomicLong();
    private final AtomicLong supplierSequence = new AtomicLong();
    private final AtomicLong storeSequence = new AtomicLong();
    private final AtomicLong saleSequence = new AtomicLong();
    private final AtomicLong saleItemSequence = new AtomicLong();
    private final AtomicLong userSequence = new AtomicLong();

    public long nextProductId()      { return productSequence.incrementAndGet(); }
    public long nextCategoryId()     { return categorySequence.incrementAndGet(); }
    public long nextSupplierId()     { return supplierSequence.incrementAndGet(); }
    public long nextStoreId()        { return storeSequence.incrementAndGet(); }
    public long nextSaleId()         { return saleSequence.incrementAndGet(); }
    public long nextSaleItemId()     { return saleItemSequence.incrementAndGet(); }
    public long nextUserId()         { return userSequence.incrementAndGet(); }
}