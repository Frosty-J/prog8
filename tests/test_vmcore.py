import pytest
from tinyvm.core import Memory


def test_memory_unsigned():
    m = Memory()
    m.set_byte(1000, 1)
    m.set_byte(1001, 2)
    m.set_byte(1002, 3)
    m.set_byte(1003, 4)
    m.set_byte(2000, 252)
    m.set_byte(2001, 253)
    m.set_byte(2002, 254)
    m.set_byte(2003, 255)
    assert 1 == m.get_byte(1000)
    assert 2 == m.get_byte(1001)
    assert 3 == m.get_byte(1002)
    assert 4 == m.get_byte(1003)
    assert 252 == m.get_byte(2000)
    assert 253 == m.get_byte(2001)
    assert 254 == m.get_byte(2002)
    assert 255 == m.get_byte(2003)
    assert b"\x01\x02\x03\x04" == m.get_bytes(1000, 4)
    assert 0x0201 == m.get_word(1000)
    assert 0xfffe == m.get_word(2002)
    m.set_word(2002, 40000)
    assert 40000 == m.get_word(2002)
    assert 0x40 == m.get_byte(2002)
    assert 0x9c == m.get_byte(2003)


def test_memory_signed():
    m = Memory()
    m.set_byte(1000, 1)
    m.set_byte(1001, 2)
    m.set_byte(1002, 3)
    m.set_byte(1003, 4)
    m.set_byte(2000, 252)
    m.set_byte(2001, 253)
    m.set_byte(2002, 254)
    m.set_byte(2003, 255)
    assert 1 == m.get_sbyte(1000)
    assert 2 == m.get_sbyte(1001)
    assert 3 == m.get_sbyte(1002)
    assert 4 == m.get_sbyte(1003)
    assert -4 == m.get_sbyte(2000)
    assert -3 == m.get_sbyte(2001)
    assert -2 == m.get_sbyte(2002)
    assert -1 == m.get_sbyte(2003)
    assert 0x0201 == m.get_sword(1000)
    assert -2 == m.get_sword(2002)
    m.set_sword(2002, 30000)
    assert 30000 == m.get_sword(2002)
    assert 0x30 == m.get_sbyte(2002)
    assert 0x75 == m.get_sbyte(2003)
    m.set_sword(2002, -30000)
    assert -30000 == m.get_sword(2002)
    assert 0x8ad0 == m.get_word(2002)
    assert 0xd0 == m.get_byte(2002)
    assert 0x8a == m.get_byte(2003)
